package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.dto.*;
import com.openclassrooms.projet3.model.RentalModel;
import com.openclassrooms.projet3.model.UserModel;
import com.openclassrooms.projet3.service.RentalService;
import com.openclassrooms.projet3.service.UserService;
import com.openclassrooms.projet3.utils.DateUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

// Définition d'un controlleur REST pour la gestion des locations
// Gère tout les endpoints commencant par '/api/rentals'
@RestController
public class RentalController {

    // Injection et initalisation des dépendances néccessaires
    private final String uploadDir = System.getProperty("user.dir") + "/uploads";
    @Autowired
    private RentalService rentalService;
    @Autowired
    private UserService userService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;

        // Crée le dossier d'uploads s'il n'existe pas
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
    }

    /**
     * Déclaration de la route de récupération des locations
     * Read - Get all rentals
     *
     * @return - An Iterable object of GetRentalByIdDto items
     */
    @GetMapping("/api/rentals")
    public ResponseEntity<GetAllRentalsResponseDto> getRentals() {
        //Deamnde au service de la liste des locations en base
        Iterable<RentalModel> rentals = rentalService.getRentals();

        // Création d'une liste de DTO pour la réponse
        List<GetRentalByIdDtoResponse> dtoList = new ArrayList<>();

        // Pour chaque location, on crée le DTO correspondant et on l'ajoute a la liste de la réponse
        for (RentalModel rental : rentals) {
            GetRentalByIdDtoResponse getRentalByIdDtoResponse = new GetRentalByIdDtoResponse();
            getRentalByIdDtoResponse.setId(rental.getId());
            getRentalByIdDtoResponse.setName(rental.getName());
            getRentalByIdDtoResponse.setSurface(rental.getSurface());
            getRentalByIdDtoResponse.setPrice(rental.getPrice());
            getRentalByIdDtoResponse.setPicture(rental.getPicture());
            getRentalByIdDtoResponse.setDescription(rental.getDescription());
            getRentalByIdDtoResponse.setOwnerId(rental.getOwner_id());
            getRentalByIdDtoResponse.setCreatedAt(DateUtils.formatTimestamp(rental.getCreated_at()));
            getRentalByIdDtoResponse.setUpdatedAt(DateUtils.formatTimestamp(rental.getUpdated_at()));

            dtoList.add(getRentalByIdDtoResponse);
        }

        GetAllRentalsResponseDto response = new GetAllRentalsResponseDto(dtoList);

        // On renvoi la réponse
        return ResponseEntity.ok(response);
    }

    /**
     * Déclaration de la route de récuperation d'une location via son id
     * Read - Get a specific rental from id
     *
     * @param id of the rental needed
     * @return A GetRentalByIdDto of the rental object
     */
    @GetMapping("/api/rentals/{id}")
    public ResponseEntity<Object> getRentalById(@PathVariable Long id) {
        // Récupération de la location via l'id fournit
        Optional<RentalModel> rentalOpt = rentalService.getRental(id);

        // Si il n'existe pas on retourne une erreur
        if (rentalOpt.isEmpty()) {
            String res = "L'id demandé ne corresponds a aucune location.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }

        //Sinon on récupere le RentalModel correspondant
        RentalModel rental = rentalOpt.get();

        //Puis on génère le DTO de la réponse
        GetRentalByIdDtoResponse getRentalByIdDtoResponse = new GetRentalByIdDtoResponse();
        getRentalByIdDtoResponse.setId(rental.getId());
        getRentalByIdDtoResponse.setName(rental.getName());
        getRentalByIdDtoResponse.setSurface(rental.getSurface());
        getRentalByIdDtoResponse.setPrice(rental.getPrice());
        getRentalByIdDtoResponse.setPicture(rental.getPicture());
        getRentalByIdDtoResponse.setDescription(rental.getDescription());
        getRentalByIdDtoResponse.setOwnerId(rental.getOwner_id());
        getRentalByIdDtoResponse.setCreatedAt(DateUtils.formatTimestamp(rental.getCreated_at()));
        getRentalByIdDtoResponse.setUpdatedAt(DateUtils.formatTimestamp(rental.getUpdated_at()));

        // On renvoi la réponse
        return ResponseEntity.ok(getRentalByIdDtoResponse);
    }

    /**
     * Déclaration de la route de création d'une location
     * Create - Add a new rental
     *
     * @param createRentalDtoValidation A CreateRentalDto object
     * @return A CreateRentalDtoResponse of the new rental object
     */
    @PostMapping(path = "/api/rentals", consumes = "multipart/form-data")
    public ResponseEntity<Object> createRental(
            @ModelAttribute @Valid CreateRentalDtoValidation createRentalDtoValidation,
            @AuthenticationPrincipal Jwt jwt,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String res = "Une erreur a été detectée lors de la validation des informations renseignées.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }

        // On essaye de récuperer l'utilisateur a partir de l'id contenu dans son JWT
        String id = jwt.getClaimAsString("uid");
        Optional<UserModel> userOpt = userService.getUser(Long.valueOf(id));
        // Si il n'existe pas, on renvoi une erreur
        if (userOpt.isEmpty()) {
            String res = "Vous devez etre connecté pour réaliser cette action.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }
        //Sinon on récupère le UserModel correspondant
        UserModel user = userOpt.get();

        // Si l'image est manquante on retourne une erreur
        MultipartFile pictureFile = createRentalDtoValidation.getPicture();
        if (pictureFile.isEmpty()) {
            String res = "Un fichier image est requis.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }

        String savedFileName = null;
        // Gestion de l'enregistrement de l'image dans le systeme de fichiers
        try {
            String extension = "";
            String originalName = pictureFile.getOriginalFilename();

            String contentType = pictureFile.getContentType();
            if (contentType.isEmpty() || !contentType.startsWith("image/")) {
                String res = "Un fichier image est requis.";
                Map<String, Object> json = new HashMap<>();
                json.put("message", res);
                return ResponseEntity.badRequest().body(json);
            }

            // On crée un nom de fichier a partir d'une chaine aléatoire et de l'extension
            extension = originalName.substring(originalName.lastIndexOf("."));
            savedFileName = UUID.randomUUID() + extension;

            // Sauvegarder sur le disque
            File dest = new File(uploadDir + File.separator + savedFileName);
            pictureFile.transferTo(dest);
        } catch (IOException e) {
            // En cas d'erreur, on retourne le prolème rencontré
            String res = "Erreur : " + e.getMessage();
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }

        // Création de l'url du fichier qui sera stocké en base
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String fileUrl = baseUrl + "/uploads/" + savedFileName;

        // On crée le RentalModel correspondant aux données recues
        RentalModel newRental = new RentalModel();
        newRental.setName(createRentalDtoValidation.getName());
        newRental.setSurface(createRentalDtoValidation.getSurface());
        newRental.setPrice(createRentalDtoValidation.getPrice());
        newRental.setPicture(fileUrl);
        newRental.setDescription(createRentalDtoValidation.getDescription());
        newRental.setOwner_id(Math.toIntExact(user.getId()));
        newRental.setCreated_at(new Timestamp(System.currentTimeMillis()));
        newRental.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        // On l'enregistre en base
        RentalModel savedRental = rentalService.saveRental(newRental);

        // On prépare la réponse
        CreateRentalDtoResponse createRentalDtoResponse = new CreateRentalDtoResponse();
        createRentalDtoResponse.setId(savedRental.getId());
        createRentalDtoResponse.setName(savedRental.getName());
        createRentalDtoResponse.setSurface(savedRental.getSurface());
        createRentalDtoResponse.setPrice(savedRental.getPrice());
        createRentalDtoResponse.setPicture(savedRental.getPicture());
        createRentalDtoResponse.setDescription(savedRental.getDescription());
        createRentalDtoResponse.setOwnerId(savedRental.getOwner_id());
        createRentalDtoResponse.setCreatedAt(DateUtils.formatTimestamp(savedRental.getCreated_at()));
        createRentalDtoResponse.setUpdatedAt(DateUtils.formatTimestamp(savedRental.getUpdated_at()));

        // On retourne la réponse
        String res = "Rental created successfully";
        Map<String, Object> json = new HashMap<>();
        json.put("message", res);
        return ResponseEntity.ok(json);
    }

    /**
     * Déclaration de la route de mise a jour d'une location
     * Update - Update an existing rental
     *
     * @param updateRentalDtoValidation A UpdateRentalDtoValidation object
     * @return A UpdateRentalDtoValidation of the rental object
     */
    @PutMapping(path = "/api/rentals/{idRental}", consumes = "multipart/form-data")
    public ResponseEntity<Object> updateRental(
            @PathVariable Long idRental,
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute @Valid UpdateRentalDtoValidation updateRentalDtoValidation,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            String res = "Une erreur a été detectée lors de la validation des informations renseignées.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }

        //on essaye de récuperer la location a partir de l'id fournit
        Optional<RentalModel> rentalOpt = rentalService.getRental(idRental);
        if (rentalOpt.isEmpty()) {
            String res = "L'id demandé ne corresponds a aucune location.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }

        //on recupere le RentalModel correspondant
        RentalModel rental = rentalOpt.get();

        //recupere l'user courant a partir de l'id inclut dans son JWT
        String idOwner = jwt.getClaimAsString("uid");
        Optional<UserModel> userOpt = userService.getUser(Long.valueOf(idOwner));
        if (userOpt.isEmpty()) {
            String res = "Vous devez etre connecté pour effectuer cette action.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }
        // On récupere le UserModel correspondant
        UserModel user = userOpt.get();

        // On verifie si l user courant est bien l owner ID de la location
        if (!user.getId().equals(rental.getOwner_id().longValue())) {
            String res = "Vous devez etre le créateur de cette location pour pouvoir la modifier";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }

        // Mise a jour et sauvegarde dans la base
        RentalModel updatedRental = rentalService.update(updateRentalDtoValidation, rental);

        // Transformation en DTO de sortie
        UpdateRentalDtoResponse updateRentalDtoResponse = new UpdateRentalDtoResponse();
        updateRentalDtoResponse.setId(updatedRental.getId());
        updateRentalDtoResponse.setName(updatedRental.getName());
        updateRentalDtoResponse.setSurface(updatedRental.getSurface());
        updateRentalDtoResponse.setPrice(updatedRental.getPrice());
        updateRentalDtoResponse.setPicture(updatedRental.getPicture());
        updateRentalDtoResponse.setDescription(updatedRental.getDescription());
        updateRentalDtoResponse.setOwnerId(updatedRental.getOwner_id());
        updateRentalDtoResponse.setCreatedAt(DateUtils.formatTimestamp(updatedRental.getCreated_at()));
        updateRentalDtoResponse.setUpdatedAt(DateUtils.formatTimestamp(updatedRental.getUpdated_at()));

        // renvoi du dto de réponse
        String res = "Rental updated successfully";
        Map<String, Object> json = new HashMap<>();
        json.put("message", res);
        return ResponseEntity.ok(json);
    }
}
