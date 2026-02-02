package com.ecommerce.controller.api.admin;

import com.ecommerce.model.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/admin/media")
public class MediaController {

    private static final String ROOT_DIR = "uploads/";

    @GetMapping("/files")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> listFiles(
            @RequestParam(defaultValue = "") String path) {
        try {
            Path dirPath = getSecurePath(path);
            if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Directory does not exist"));
            }

            try (Stream<Path> stream = Files.list(dirPath)) {
                List<Map<String, Object>> files = stream
                        .map(p -> {
                            Map<String, Object> map = new HashMap<>();
                            File f = p.toFile();
                            map.put("name", f.getName());
                            map.put("type", f.isDirectory() ? "folder" : "file");
                            map.put("path", getRelativePath(p));
                            if (!f.isDirectory()) {
                                map.put("url", "/uploads/" + getRelativePath(p).replace("\\", "/"));
                                map.put("size", f.length());
                            }
                            return map;
                        })
                        .sorted((a, b) -> {
                            // Folders first
                            String typeA = (String) a.get("type");
                            String typeB = (String) b.get("type");
                            if (typeA.equals(typeB))
                                return ((String) a.get("name")).compareTo((String) b.get("name"));
                            return typeA.equals("folder") ? -1 : 1;
                        })
                        .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.success(files));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to list files: " + e.getMessage()));
        }
    }

    @PostMapping("/folder")
    public ResponseEntity<ApiResponse<Void>> createFolder(@RequestBody Map<String, String> payload) {
        try {
            String path = payload.getOrDefault("path", "");
            String name = payload.get("name");
            if (name == null || name.isEmpty())
                return ResponseEntity.badRequest().body(ApiResponse.error("Folder name required"));

            Path parentPath = getSecurePath(path);

            // Auto-numbering: generate unique folder name if exists
            String uniqueName = generateUniqueName(parentPath, name);
            Path dirPath = parentPath.resolve(uniqueName);

            Files.createDirectories(dirPath);
            return ResponseEntity.ok(ApiResponse.success("Folder created", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to create folder"));
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "") String path,
            @RequestParam(defaultValue = "false") boolean overwrite) {
        try {
            if (file.isEmpty())
                return ResponseEntity.badRequest().body(ApiResponse.error("File empty"));

            Path dirPath = getSecurePath(path);
            if (!Files.exists(dirPath))
                Files.createDirectories(dirPath);

            String filename = file.getOriginalFilename();
            if (filename == null)
                filename = "unnamed_file";

            // Basic sanitization
            filename = filename.replaceAll("[^a-zA-Z0-9._-]", "_");

            // Auto-numbering: generate unique filename if exists
            String uniqueFilename = generateUniqueName(dirPath, filename);
            Path filePath = dirPath.resolve(uniqueFilename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return web-friendly URL
            String webPath = getRelativePath(filePath).replace("\\", "/");
            return ResponseEntity.ok(ApiResponse.success("Uploaded", "/uploads/" + webPath));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Upload failed: " + e.getMessage()));
        }
    }

    @PatchMapping("/rename")
    public ResponseEntity<ApiResponse<Void>> rename(@RequestBody Map<String, String> payload) {
        try {
            String oldPath = payload.get("path");
            String newName = payload.get("newName");

            if (oldPath == null || oldPath.isEmpty())
                return ResponseEntity.badRequest().body(ApiResponse.error("Path is required"));
            if (newName == null || newName.isEmpty())
                return ResponseEntity.badRequest().body(ApiResponse.error("New name is required"));

            // Sanitize new name
            newName = newName.replaceAll("[^a-zA-Z0-9._-]", "_");

            Path sourcePath = getSecurePath(oldPath);
            if (!Files.exists(sourcePath))
                return ResponseEntity.badRequest().body(ApiResponse.error("File or folder not found"));

            // Get parent directory
            Path parentPath = sourcePath.getParent();
            if (parentPath == null)
                return ResponseEntity.badRequest().body(ApiResponse.error("Cannot rename root directory"));

            Path targetPath = parentPath.resolve(newName);

            // Check if target already exists
            if (Files.exists(targetPath))
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("A file or folder with this name already exists"));

            // Perform rename
            Files.move(sourcePath, targetPath, StandardCopyOption.ATOMIC_MOVE);

            return ResponseEntity.ok(ApiResponse.success("Renamed successfully", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Rename failed: " + e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> delete(@RequestParam String path) {
        try {
            Path target = getSecurePath(path);
            if (!Files.exists(target))
                return ResponseEntity.badRequest().body(ApiResponse.error("File or folder not found"));

            // Recursive delete for directories
            deleteRecursively(target);

            return ResponseEntity.ok(ApiResponse.success("Deleted", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Delete failed: " + e.getMessage()));
        }
    }

    /**
     * Generate unique name by appending (1), (2), etc. if name already exists
     */
    private String generateUniqueName(Path directory, String baseName) {
        String name = baseName;
        String extension = "";

        // Extract extension for files
        int dotIndex = baseName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < baseName.length() - 1) {
            name = baseName.substring(0, dotIndex);
            extension = baseName.substring(dotIndex);
        }

        Path targetPath = directory.resolve(baseName);
        int counter = 1;

        while (Files.exists(targetPath)) {
            String newName = name + " (" + counter + ")" + extension;
            targetPath = directory.resolve(newName);
            counter++;
        }

        return targetPath.getFileName().toString();
    }

    /**
     * Recursively delete a file or directory
     */
    private void deleteRecursively(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.walk(path)) {
                entries.sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        });
            }
        } else {
            Files.delete(path);
        }
    }

    private Path getSecurePath(String relativePath) throws IOException {
        Path root = Paths.get(ROOT_DIR).toAbsolutePath().normalize();
        if (!Files.exists(root))
            Files.createDirectories(root);

        // Remove leading/trailing slashes
        if (relativePath.startsWith("/"))
            relativePath = relativePath.substring(1);

        Path resolved = root.resolve(relativePath).normalize();
        if (!resolved.startsWith(root)) {
            throw new SecurityException("Invalid path");
        }
        return resolved;
    }

    private String getRelativePath(Path fullPath) {
        Path root = Paths.get(ROOT_DIR).toAbsolutePath().normalize();
        return root.relativize(fullPath).toString();
    }
}
