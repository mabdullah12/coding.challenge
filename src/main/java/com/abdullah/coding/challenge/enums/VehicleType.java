package com.abdullah.coding.challenge.enums;

public enum VehicleType {
    BIKE, SEDAN, SUV;

    public static VehicleType getVehicleType(String vehicleType) {
        for (VehicleType type : VehicleType.values()) {
            if (type.name().equalsIgnoreCase(vehicleType))
                return type;
        }
        return null;
    }

}
