package com.openclassrooms.projet3.dto;

import java.util.List;

public class GetAllRentalsResponseDto {
    private List<GetRentalByIdDtoResponse> rentals;


    public GetAllRentalsResponseDto(List<GetRentalByIdDtoResponse> rentals) {
        this.rentals = rentals;
    }

    public List<GetRentalByIdDtoResponse> getRentals() {
        return rentals;
    }

    public void setRentals(List<GetRentalByIdDtoResponse> rentals) {
        this.rentals = rentals;
    }
}
