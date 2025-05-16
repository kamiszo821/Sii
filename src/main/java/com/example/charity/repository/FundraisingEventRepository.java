package com.example.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.charity.model.FundraisingEvent;

public interface FundraisingEventRepository extends JpaRepository<FundraisingEvent, Long> {

}
