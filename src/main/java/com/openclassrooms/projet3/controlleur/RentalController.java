package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.model.RentalModel;
import com.openclassrooms.projet3.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;


@RestController
public class RentalController {

    @Autowired
    private RentalService rentalService;

    private static final String UPLOAD_DIR = "uploads/";

    /**
     * Read - Get all rentals
     * @return - An Iterable object of Rental fulfilled
     * testé et OK
     */
    @GetMapping("/rentals")
    public Iterable<RentalModel> getRentals(){
        return rentalService.getRentals();
    }

    /**
     * Read - Get a specific rental from id
     * @return - An object of Rental type
     * testé et OK
     */
    @GetMapping("/rentals/{id}")
    public Optional<RentalModel> getRentalById(@PathVariable Long id){
        return rentalService.getRental(id);
    }

    /**
     * Upload - upload an image file
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadRental(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("Aucun fichier reçu");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new Exception("Le fichier n'est pas une image valide");
        }

        try{
            //création d'un nom de fichier aléatoire
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            int i = originalFilename.lastIndexOf('.');
            if (i > 0) {
                extension = originalFilename.substring(i);
            }
            String randomFileName = UUID.randomUUID() + extension;
            Path path = Paths.get(UPLOAD_DIR + randomFileName);
            //ecriture du fichier
            Files.write(path, file.getBytes());

            return ResponseEntity.ok("Fichier uploadé avec succès : " + path);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur lors de l'upload : " + e.getMessage());
        }
    }

    /**
     * Create - Add a new rental
     * @param rental A Rental object
     * @return The rental object saved
     */
    @PostMapping("/rentals")
    public RentalModel createRental(@RequestBody RentalModel rental){
        return rentalService.saveRental(rental);
    }

    @PutMapping("/rentals/{id}")
    public RentalModel  updateRental(@PathVariable Long id, @RequestBody RentalModel rental){
        return rentalService.update(id, rental);
    }

    /**
     * Delete - Remove a rental
     * @param id - The id of the rental to delete
     */
    @DeleteMapping("/rentals/{id}")
    public void deleteRental(@PathVariable Long id){
        rentalService.deleteRental(id);
    }
}
