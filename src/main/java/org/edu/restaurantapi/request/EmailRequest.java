package org.edu.restaurantapi.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequest {
    String to; // người nhận
    String subject; // tiêu đề
    String text; // nội dung
}
