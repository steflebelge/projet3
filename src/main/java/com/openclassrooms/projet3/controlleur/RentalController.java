package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.dto.getRentalByIdDto;
import com.openclassrooms.projet3.dto.updateRentalDto;
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
     * @return - An Iterable object of Rental fulfilled
     * testé et OK
     */
    @GetMapping("/rentals")
    public ResponseEntity<List<getRentalByIdDto>> getRentals() {
        Iterable<RentalModel> rentals = rentalService.getRentals();

        List<getRentalByIdDto> dtoList = new ArrayList<>();
        for (RentalModel rental : rentals) {
            getRentalByIdDto dto = new getRentalByIdDto();
            dto.setId(rental.getId());
            dto.setName(rental.getName());
            dto.setSurface(rental.getSurface());
            dto.setPrice(rental.getPrice());
            dto.setPicture(rental.getPicture());
            dto.setDescription(rental.getDescription());
            dto.setOwnerId(rental.getOwner_id());
            dto.setCreatedAt(rental.getCreated_at());
            dto.setUpdatedAt(rental.getUpdated_at());

            dtoList.add(dto);
        }

        return ResponseEntity.ok(dtoList);
    }

    /**
     * Read - Get a specific rental from id
     * @return - An object of Rental type
     * testé et OK
     */
    @GetMapping("/rentals/{id}")
    public ResponseEntity<getRentalByIdDto> getRentalById(@PathVariable Long id){
        Optional<RentalModel> rentalOpt = rentalService.getRental(id);
        if(rentalOpt.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        RentalModel rental = rentalOpt.get();
        getRentalByIdDto dto = new getRentalByIdDto();
        dto.setId(rental.getId());
        dto.setName(rental.getName());
        dto.setSurface(rental.getSurface());
        dto.setPrice(rental.getPrice());
        dto.setPicture(rental.getPicture());
        dto.setDescription(rental.getDescription());
        dto.setOwnerId(rental.getOwner_id());
        dto.setCreatedAt(rental.getCreated_at());
        dto.setUpdatedAt(rental.getUpdated_at());

        return ResponseEntity.ok(dto);
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
     * @param newRental A Rental object
     * @return The rental object saved
     */
    @PostMapping("/rentals")
    public ResponseEntity<getRentalByIdDto> createRental(@RequestBody RentalModel newRental){
        RentalModel rentalOpt = rentalService.saveRental(newRental);

        getRentalByIdDto dto = new getRentalByIdDto();
        dto.setId(rentalOpt.getId());
        dto.setName(rentalOpt.getName());
        dto.setSurface(rentalOpt.getSurface());
        dto.setPrice(rentalOpt.getPrice());
        dto.setPicture(rentalOpt.getPicture());
        dto.setDescription(rentalOpt.getDescription());
        dto.setOwnerId(rentalOpt.getOwner_id());
        dto.setCreatedAt(rentalOpt.getCreated_at());
        dto.setUpdatedAt(rentalOpt.getUpdated_at());

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/rentals/{id}")
    public ResponseEntity<getRentalByIdDto> updateRental(@PathVariable Long id, @RequestBody @Valid updateRentalDto updateRentalDto){

        Optional<RentalModel> rentalOpt = rentalService.getRental(id);
        if (rentalOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RentalModel rental = rentalOpt.get();

        // Mise à jour manuelle uniquement des champs non nuls du DTO
        if (updateRentalDto.getName() != null) rental.setName(updateRentalDto.getName());
        if (updateRentalDto.getSurface() != null) rental.setSurface(updateRentalDto.getSurface());
        if (updateRentalDto.getPrice() != null) rental.setPrice(updateRentalDto.getPrice());
        if (updateRentalDto.getPicture() != null) rental.setPicture(updateRentalDto.getPicture());
        if (updateRentalDto.getDescription() != null) rental.setDescription(updateRentalDto.getDescription());
        if (updateRentalDto.getOwnerId() != null) rental.setOwner_id(updateRentalDto.getOwnerId());
        // Mettre à jour la date de modification
        rental.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        // Sauvegarde dans la DB
        RentalModel updatedRental = rentalService.saveRental(rental);

        // Transformation en DTO de sortie
        getRentalByIdDto responseDto = new getRentalByIdDto();
        responseDto.setId(updatedRental.getId());
        responseDto.setName(updatedRental.getName());
        responseDto.setSurface(updatedRental.getSurface());
        responseDto.setPrice(updatedRental.getPrice());
        responseDto.setPicture(updatedRental.getPicture());
        responseDto.setDescription(updatedRental.getDescription());
        responseDto.setOwnerId(updatedRental.getOwner_id());
        responseDto.setCreatedAt(updatedRental.getCreated_at());
        responseDto.setUpdatedAt(updatedRental.getUpdated_at());

        return ResponseEntity.ok(responseDto);
    }

    /**
     * Delete - Remove a rental
     * @param id - The id of the rental to delete
     */
    @DeleteMapping("/rentals/{id}")
    public void deleteRental(@PathVariable Long id){
        rentalService.deleteRental(id);
        //cas id non correct
        //renvoi un 200
    }
}
