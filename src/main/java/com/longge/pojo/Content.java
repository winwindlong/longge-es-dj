package com.longge.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Content {
    private String title;
    private String shop;
    private String price;
    private String img;
}
