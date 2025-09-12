package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.dto.*;
import com.openclassrooms.projet3.model.RentalModel;
import com.openclassrooms.projet3.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
public class RentalController {

    @Autowired
    private RentalService rentalService;

    private static final String UPLOAD_DIR = "uploads/";

    /**
     * Read - Get all rentals
     * @return - An Iterable object of GetRentalByIdDto items
     */
    @GetMapping("/api/rentals")
    public ResponseEntity<List<GetRentalByIdDtoResponse>> getRentals() {
        Iterable<RentalModel> rentals = rentalService.getRentals();

        List<GetRentalByIdDtoResponse> dtoList = new ArrayList<>();
        for (RentalModel rental : rentals) {
            GetRentalByIdDtoResponse getRentalByIdDtoResponse = new GetRentalByIdDtoResponse();
            getRentalByIdDtoResponse.setId(rental.getId());
            getRentalByIdDtoResponse.setName(rental.getName());
            getRentalByIdDtoResponse.setSurface(rental.getSurface());
            getRentalByIdDtoResponse.setPrice(rental.getPrice());
            getRentalByIdDtoResponse.setPicture(rental.getPicture());
            getRentalByIdDtoResponse.setDescription(rental.getDescription());
            getRentalByIdDtoResponse.setOwnerId(rental.getOwner_id());
            getRentalByIdDtoResponse.setCreatedAt(rental.getCreated_at());
            getRentalByIdDtoResponse.setUpdatedAt(rental.getUpdated_at());

            dtoList.add(getRentalByIdDtoResponse);
        }

        return ResponseEntity.ok(dtoList);
    }

    /**
     * Read - Get a specific rental from id
     * @param id of the rental needed
     * @return A GetRentalByIdDto of the rental object
     */
    @GetMapping("/api/rentals/{id}")
    public ResponseEntity<GetRentalByIdDtoResponse> getRentalById(@PathVariable Long id){
        Optional<RentalModel> rentalOpt = rentalService.getRental(id);
        if(rentalOpt.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        RentalModel rental = rentalOpt.get();
        GetRentalByIdDtoResponse getRentalByIdDtoResponse = new GetRentalByIdDtoResponse();
        getRentalByIdDtoResponse.setId(rental.getId());
        getRentalByIdDtoResponse.setName(rental.getName());
        getRentalByIdDtoResponse.setSurface(rental.getSurface());
        getRentalByIdDtoResponse.setPrice(rental.getPrice());
        getRentalByIdDtoResponse.setPicture(rental.getPicture());
        getRentalByIdDtoResponse.setDescription(rental.getDescription());
        getRentalByIdDtoResponse.setOwnerId(rental.getOwner_id());
        getRentalByIdDtoResponse.setCreatedAt(rental.getCreated_at());
        getRentalByIdDtoResponse.setUpdatedAt(rental.getUpdated_at());

        return ResponseEntity.ok(getRentalByIdDtoResponse);
    }

    /**
     * Upload - upload an image file
     */
    @PostMapping("/api/upload")
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
     * @param createRentalDtoValidation A CreateRentalDto object
     * @return A CreateRentalDtoResponse of the new rental object
     */
    @PostMapping("/api/rentals")
    public ResponseEntity<CreateRentalDtoResponse> createRental(@RequestBody @Valid CreateRentalDtoValidation createRentalDtoValidation){
        RentalModel newRental = new RentalModel();
        newRental.setName(createRentalDtoValidation.getName());
        newRental.setSurface(createRentalDtoValidation.getSurface());
        newRental.setPrice(createRentalDtoValidation.getPrice());
        newRental.setPicture(createRentalDtoValidation.getPicture());
        newRental.setDescription(createRentalDtoValidation.getDescription());
        newRental.setOwner_id(createRentalDtoValidation.getOwnerId());
        newRental.setCreated_at(new Timestamp(System.currentTimeMillis()));
        newRental.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        RentalModel savedRental = rentalService.saveRental(newRental);

        CreateRentalDtoResponse createRentalDtoResponse = new CreateRentalDtoResponse();
        createRentalDtoResponse.setId(savedRental.getId());
        createRentalDtoResponse.setName(savedRental.getName());
        createRentalDtoResponse.setSurface(savedRental.getSurface());
        createRentalDtoResponse.setPrice(savedRental.getPrice());
        createRentalDtoResponse.setPicture(savedRental.getPicture());
        createRentalDtoResponse.setDescription(savedRental.getDescription());
        createRentalDtoResponse.setOwnerId(savedRental.getOwner_id());
        createRentalDtoResponse.setCreatedAt(savedRental.getCreated_at());
        createRentalDtoResponse.setUpdatedAt(savedRental.getUpdated_at());

        return ResponseEntity.ok(createRentalDtoResponse);
    }

    /**
     * Update - Update an existing rental
     * @param updateRentalDtoValidation A UpdateRentalDtoValidation object
     * @return A UpdateRentalDtoValidation of the rental object
     */
    @PutMapping("/api/rentals/{id}")
    public ResponseEntity<UpdateRentalDtoResponse> updateRental(@PathVariable Long id, @RequestBody @Valid UpdateRentalDtoValidation updateRentalDtoValidation){

        Optional<RentalModel> rentalOpt = rentalService.getRental(id);
        if (rentalOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RentalModel rental = rentalOpt.get();

        // Mise à jour manuelle uniquement des champs non nuls du DTO
        if (updateRentalDtoValidation.getName() != null) rental.setName(updateRentalDtoValidation.getName());
        if (updateRentalDtoValidation.getSurface() != null) rental.setSurface(updateRentalDtoValidation.getSurface());
        if (updateRentalDtoValidation.getPrice() != null) rental.setPrice(updateRentalDtoValidation.getPrice());
        if (updateRentalDtoValidation.getPicture() != null) rental.setPicture(updateRentalDtoValidation.getPicture());
        if (updateRentalDtoValidation.getDescription() != null) rental.setDescription(updateRentalDtoValidation.getDescription());
        if (updateRentalDtoValidation.getOwnerId() != null) rental.setOwner_id(updateRentalDtoValidation.getOwnerId());
        // Mettre à jour la date de modification
        rental.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        // Sauvegarde dans la DB
        RentalModel updatedRental = rentalService.saveRental(rental);

        // Transformation en DTO de sortie
        UpdateRentalDtoResponse updateRentalDtoResponse = new UpdateRentalDtoResponse();
        updateRentalDtoResponse.setId(updatedRental.getId());
        updateRentalDtoResponse.setName(updatedRental.getName());
        updateRentalDtoResponse.setSurface(updatedRental.getSurface());
        updateRentalDtoResponse.setPrice(updatedRental.getPrice());
        updateRentalDtoResponse.setPicture(updatedRental.getPicture());
        updateRentalDtoResponse.setDescription(updatedRental.getDescription());
        updateRentalDtoResponse.setOwnerId(updatedRental.getOwner_id());
        updateRentalDtoResponse.setCreatedAt(updatedRental.getCreated_at());
        updateRentalDtoResponse.setUpdatedAt(updatedRental.getUpdated_at());

        return ResponseEntity.ok(updateRentalDtoResponse);
    }

    /**
     * Delete - Remove a rental
     *
     * @param id - The id of the rental to delete
     * @return 404 or 200
     */
    @DeleteMapping("/api/rentals/{id}")
    public ResponseEntity<Object> deleteRental(@PathVariable Long id){

        Optional<RentalModel> rentalOpt = rentalService.getRental(id);
        if (rentalOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RentalModel rental = rentalOpt.get();

        rentalService.deleteRental(rental);
        return ResponseEntity.ok().build();
    }
}
