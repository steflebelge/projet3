package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.dto.*;
import com.openclassrooms.projet3.model.RentalModel;
import com.openclassrooms.projet3.model.UserModel;
import com.openclassrooms.projet3.service.RentalService;
import com.openclassrooms.projet3.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
public class RentalController {

    private final String uploadDir = System.getProperty("user.dir") + "/uploads";
    @Autowired
    private RentalService rentalService;
    @Autowired
    private UserService userService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;

        // Crée le dossier s'il n'existe pas
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
    }

    /**
     * Read - Get all rentals
     *
     * @return - An Iterable object of GetRentalByIdDto items
     */
    @GetMapping("/api/rentals")
    public ResponseEntity<GetAllRentalsResponseDto> getRentals() {
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

        GetAllRentalsResponseDto response = new GetAllRentalsResponseDto(dtoList);

        return ResponseEntity.ok(response);
    }

    /**
     * Read - Get a specific rental from id
     *
     * @param id of the rental needed
     * @return A GetRentalByIdDto of the rental object
     */
    @GetMapping("/api/rentals/{id}")
    public ResponseEntity<GetRentalByIdDtoResponse> getRentalById(@PathVariable Long id) {
        Optional<RentalModel> rentalOpt = rentalService.getRental(id);
        if (rentalOpt.isEmpty()) {
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
     * Create - Add a new rental
     *
     * @param createRentalDtoValidation A CreateRentalDto object
     * @return A CreateRentalDtoResponse of the new rental object
     */
    @PostMapping(path = "/api/rentals", consumes = "multipart/form-data")
    public ResponseEntity<CreateRentalDtoResponse> createRental(
            @ModelAttribute @Valid CreateRentalDtoValidation createRentalDtoValidation,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String id = jwt.getClaimAsString("uid");
        Optional<UserModel> userOpt = userService.getUser(Long.valueOf(id));
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        UserModel user = userOpt.get();

        String savedFileName = null;
        // Si l'image est manquante
        MultipartFile pictureFile = createRentalDtoValidation.getPicture();
        if (pictureFile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Générer un nom unique pour éviter les collisions
            String extension = "";
            String originalName = pictureFile.getOriginalFilename();
            if (originalName.isEmpty() || !originalName.contains(".")) {
                return ResponseEntity.badRequest().build();
            }
            extension = originalName.substring(originalName.lastIndexOf("."));
            savedFileName = UUID.randomUUID() + extension;

            // Sauvegarder sur le disque
            File dest = new File(uploadDir + File.separator + savedFileName);
            pictureFile.transferTo(dest);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String fileUrl = baseUrl + "/uploads/" + savedFileName;

        RentalModel newRental = new RentalModel();
        newRental.setName(createRentalDtoValidation.getName());
        newRental.setSurface(createRentalDtoValidation.getSurface());
        newRental.setPrice(createRentalDtoValidation.getPrice());
        newRental.setPicture(fileUrl);
        newRental.setDescription(createRentalDtoValidation.getDescription());
        newRental.setOwner_id(Math.toIntExact(user.getId()));
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
     *
     * @param updateRentalDtoValidation A UpdateRentalDtoValidation object
     * @return A UpdateRentalDtoValidation of the rental object
     */
    @PutMapping(path = "/api/rentals/{id}", consumes = "multipart/form-data")
    public ResponseEntity<UpdateRentalDtoResponse> updateRental(
            @PathVariable Long id,
            @ModelAttribute @Valid UpdateRentalDtoValidation updateRentalDtoValidation) {

        Optional<RentalModel> rentalOpt = rentalService.getRental(id);
        if (rentalOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RentalModel rental = rentalOpt.get();

        // verifie si l user courant est l owner ID

        String savedFileName = null;
        // Si l'image est présente
        MultipartFile pictureFile = updateRentalDtoValidation.getPicture();
        String fileUrl = null;
        if (!pictureFile.isEmpty()) {
            try {
                // Générer un nom unique pour éviter les collisions
                String extension = "";
                String originalName = pictureFile.getOriginalFilename();
                if (originalName.isEmpty() || !originalName.contains(".")) {
                    return ResponseEntity.badRequest().build();
                }
                extension = originalName.substring(originalName.lastIndexOf("."));
                savedFileName = UUID.randomUUID() + extension;

                // Sauvegarder sur le disque
                File dest = new File(uploadDir + File.separator + savedFileName);
                pictureFile.transferTo(dest);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }

            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            fileUrl = baseUrl + "/uploads/" + savedFileName;
        }

        // Mise à jour manuelle uniquement des champs non nuls du DTO
        if (updateRentalDtoValidation.getName() != null) rental.setName(updateRentalDtoValidation.getName());
        if (updateRentalDtoValidation.getSurface() != null) rental.setSurface(updateRentalDtoValidation.getSurface());
        if (updateRentalDtoValidation.getPrice() != null) rental.setPrice(updateRentalDtoValidation.getPrice());
        if (!pictureFile.isEmpty()) rental.setPicture(fileUrl);
        if (updateRentalDtoValidation.getDescription() != null)
            rental.setDescription(updateRentalDtoValidation.getDescription());
//        if (updateRentalDtoValidation.getOwnerId() != null) rental.setOwner_id(updateRentalDtoValidation.getOwnerId());
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
    public ResponseEntity<Object> deleteRental(@PathVariable Long id) {

        Optional<RentalModel> rentalOpt = rentalService.getRental(id);
        if (rentalOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // verifie si l user courant est l owner ID

        RentalModel rental = rentalOpt.get();

        rentalService.deleteRental(rental);
        return ResponseEntity.ok().build();
    }
}
