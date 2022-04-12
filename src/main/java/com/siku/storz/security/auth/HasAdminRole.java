package com.siku.storz.security.auth;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.siku.storz.security.auth.Roles.ADMIN_ROLE;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('" + ADMIN_ROLE + "')")
public @interface HasAdminRole {
}


