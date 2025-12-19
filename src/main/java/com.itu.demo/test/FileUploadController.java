package com.itu.demo.test;

import com.itu.demo.Url;
import com.itu.demo.ModelView;
import com.itu.demo.FileUpload;
import com.itu.demo.annotations.Controller;
import com.itu.demo.annotations.RequestParam;
import com.itu.demo.annotations.PostMapping;
import java.util.Map;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
public class FileUploadController {
    
    @PostMapping("/upload-file")
    public ModelView uploadFile(
        @RequestParam("files") Map<String, FileUpload> files,
        Map<String, Object> formData
    ) {
        ModelView mv = new ModelView("uploadResult.jsp");
        
        // Ajouter tous les fichiers uploadés au modèle
        mv.addItem("uploadedFiles", files);
        
        // Traiter et optionnellement sauvegarder chaque fichier
        for (Map.Entry<String, FileUpload> entry : files.entrySet()) {
            String fieldName = entry.getKey();
            FileUpload file = entry.getValue();
            
            System.out.println("Fichier reçu - Champ: " + fieldName + ", Nom: " + file.getFileName() + ", Taille: " + file.getSize() + " octets");
            
            // Optionnel : sauvegarder le fichier sur le serveur
            // saveFile(file);
        }
        
        // Ajouter les autres données du formulaire
        mv.addItem("formData", formData);
        
        return mv;
    }
    
    // Méthode optionnelle pour sauvegarder le fichier
    private void saveFile(FileUpload file) {
        try {
            String uploadPath = "C:/uploads/";
            java.io.File uploadDir = new java.io.File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            String filePath = uploadPath + file.getFileName();
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(file.getFileBytes());
            fos.close();
            
            System.out.println("Fichier sauvegardé: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
