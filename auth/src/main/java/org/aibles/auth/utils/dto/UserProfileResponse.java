package org.aibles.auth.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aibles.auth.utils.entity.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String id;
    private String fullname;
    private String email;
    private String address;
    private Role role;
}
