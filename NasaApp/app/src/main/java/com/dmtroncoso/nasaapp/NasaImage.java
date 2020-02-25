package com.dmtroncoso.nasaapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NasaImage {

    private String url;
    private String title;
    private String explanation;
    private String date;
}
