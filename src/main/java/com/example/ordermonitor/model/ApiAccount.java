package com.example.ordermonitor.model;

import com.example.ordermonitor.stockexch.client.ExchClient;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Configurable;

@Entity
@Table(name = "se_api_account")
@Data
@NoArgsConstructor
@Configurable(preConstruction = true)
public class ApiAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="se_api_account_id_seq")
    @SequenceGenerator(name="se_api_account_id_seq", sequenceName="se_api_account_id_seq", allocationSize=1)
    @Column(name = "id", updatable=false)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "se_id")
    private StockExchange stockExchange;
    @Column(name = "tg_username")
    private String telegramUsername;
    @Column(name = "tg_chat_id")
    private String telegramChatId;
    @Transient
    private ExchClient exchClient;

}
