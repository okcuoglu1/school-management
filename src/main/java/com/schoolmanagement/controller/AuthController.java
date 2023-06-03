package com.schoolmanagement.controller;

import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.payload.request.LoginRequest;
import com.schoolmanagement.payload.response.AuthResponse;
import com.schoolmanagement.security.jwt.JwtUtils;
import com.schoolmanagement.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    public final JwtUtils jwtUtils;
    public final AuthenticationManager authenticationManager;

    @PostMapping("/login") // http://localhost:8080/auth/login
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody @Valid LoginRequest loginRequest){

        //!!! Gelen requestin icinden kullanici adi ve parola bilgisi aliniyor
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // !!! authenticationManager uzerinden kullaniciyi valide ediyoruz.Valide edilmiş kullanıcıyı döndürüyor.
        Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        // !!! valide edilen kullanici Context e atiliyor
        SecurityContextHolder.getContext().setAuthentication(authentication);//Authentication edilen kullanıcılara ekle diyorum.
        // !!! JWT token olusturuluyor
        String token = "Bearer " + jwtUtils.generateJwtToken(authentication);

        // !!! GrantedAuthority turundeki role yapisini String turune ceviriliyor.Cünkü ön tarafa daha okunur bir sekilde gitmesi icin.
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal(); //getPrincipal() -> Anlık olarak login islemini gerceklestiren userı gönderiyor.

        Set<String> roles = userDetails
                .getAuthorities() //String e cevirmemiz gereken rolleri aldık.
                .stream() //Akışa sokuyoruz.
                .map(GrantedAuthority::getAuthority) //GrantedAuthority tipindeki rollerimizi stringe ceviriyor. ADMIN, MANAGER VB.
                .collect(Collectors.toSet());

        Optional<String> role = roles.stream().findFirst(); //İlkini al getir.Null gelme ihtimali olduğu için optional yaptık.

        // !!! AuthResponse
        AuthResponse.AuthResponseBuilder authResponse = AuthResponse.builder(); //Objeyi önce boş oluşturuyoruz. Sonra fieldları setliyoruz.
        authResponse.username(userDetails.getUsername());
        authResponse.token(token);
        authResponse.name(userDetails.getName());

        // !!! Rol mevcutsa ve TEACHER ise advisor durumu setleniyor
        if(role.isPresent()) { //Bu role var ise
            authResponse.role(role.get());
            if(role.get().equalsIgnoreCase(RoleType.TEACHER.name())) {
                authResponse.isAdvisor(userDetails.getIsAdvisor().toString());
            }
        }

        // !!! AuthResponse nesnesi ResponseEntity ile donduruyoruz
        return ResponseEntity.ok(authResponse.build());

    }

}


