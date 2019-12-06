package com.github.therycn.tyweatherwebflux.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyWeather {

    @Id
    private String id;

    @Indexed(unique = true)
    private LocalDate day;

    private String cityName;

    private float tempMin;

    private float tempMax;

    private float pressure;

    private float humidity;

    @CreatedDate
    public Date createDate;

    @LastModifiedDate
    public Date lastModifiedDate;

    public DailyWeather(LocalDate day, String cityName, float tempMin, float tempMax, float pressure, float humidity) {
        this.day = day;
        this.cityName = cityName;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.humidity = humidity;
    }
}
