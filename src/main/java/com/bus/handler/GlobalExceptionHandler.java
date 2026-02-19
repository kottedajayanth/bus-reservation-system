package com.bus.handler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bus.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ================= RESOURCE NOT FOUND =================
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(
                "error",
                ex.getMessage());

        return "redirect:/";
    }

    // ================= RUNTIME EXCEPTIONS =================
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntime(RuntimeException ex,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(
                "error",
                ex.getMessage());

        return "redirect:/";
    }

    // ================= GENERIC EXCEPTION =================
    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(
                "error",
                "Something went wrong. Please try again.");

        return "redirect:/";
    }
}
