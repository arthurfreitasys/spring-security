package com.example.ssecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LaboratorioController {

    @GetMapping("/")
    public String home(){
        return "Bem vindo ao Sistema de lab (Acesso Publico)";
    }

    @GetMapping("/horarios")
    public String verHorarios(){
        return "Grade de horarios: Lab 1 -> Java ; Lab 2 -> Python (Acesso: Aluno/Professor)";
    }

    @GetMapping("/gerenciar")
    public String gerenciarLabs(){
        return "Painel de gerenciamento de laboratorio (Acesso: Professor/Admin)";

    }

    @GetMapping("/limpeza")
    public String limpezaLab(){
        return "Painel de gerenciamento da limpeza dos laboratorios(Acesso: Funcionarios da limpeza)";
    }

}
