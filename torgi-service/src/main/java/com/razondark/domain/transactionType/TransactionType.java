package com.razondark.domain.transactionType;

import lombok.Data;

/*
    Сущность типа транзакции
*/

@Data
public class TransactionType {
    private String code; // код
    private String name; // название
    private boolean published; // статус
}
