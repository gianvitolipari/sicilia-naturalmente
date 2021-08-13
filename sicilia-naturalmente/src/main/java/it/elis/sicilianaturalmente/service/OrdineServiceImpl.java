package it.elis.sicilianaturalmente.service;

import it.elis.sicilianaturalmente.repository.OrdineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdineServiceImpl implements OrdineService{
    @Autowired
    OrdineRepository ordineRepository;
}
