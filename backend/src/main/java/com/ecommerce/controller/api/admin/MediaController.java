package com.ecommerce.controller.api.admin;

import com.ecommerce.model.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
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

            Path dirPath = getSecurePath(path).resolve(name);
            if (Files.exists(dirPath)) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Folder already exists"));
            }
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

            String filename = file.getOriginalFilename(); // Keep original name or sanitize?
            if (filename == null)
                filename = "unnamed_file";

            // Basic sanitization
            filename = filename.replaceAll("[^a-zA-Z0-9._-]", "_");

            Path filePath = dirPath.resolve(filename);

            if (Files.exists(filePath) && !overwrite) {
                return ResponseEntity.status(409).body(ApiResponse.error("File already exists"));
            }

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return web-friendly URL
            String webPath = getRelativePath(filePath).replace("\\", "/");
            return ResponseEntity.ok(ApiResponse.success("Uploaded", "/uploads/" + webPath));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Upload failed: " + e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> delete(@RequestParam String path) {
        try {
            Path target = getSecurePath(path);
            if (Files.isDirectory(target)) {
                // Recursive delete ? Or just empty? Let's safeguard to only empty for now or
                // use walkFileTree
                try (Stream<Path> entries = Files.list(target)) {
                    if (entries.findAny().isPresent()) {
                        return ResponseEntity.badRequest().body(ApiResponse.error("Folder not empty"));
                    }
                }
            }
            Files.deleteIfExists(target);
            return ResponseEntity.ok(ApiResponse.success("Deleted", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Delete failed: " + e.getMessage()));
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
