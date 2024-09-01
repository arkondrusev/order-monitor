package com.example.ordermonitor.model;

import com.example.ordermonitor.stockexch.ExchConfig;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.List;

@Entity
@Table(name = "se_api_account")
@Data
@NoArgsConstructor
public class StockExchangeApiAccount {

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
    @NonNull
    @Column(name = "tg_username")
    private String telegramUsername;
    @Transient
    private ExchConfig exchConfig;
    @Transient
    private List<StockExchangeOrder> dbOrderList;

    @PostConstruct
    private void init(@NotNull @Autowired Environment env) {
        // load api config
        String propSubstr = stockExchange.getName().toLowerCase() + ".api." + getName().toLowerCase();
        String apiKey = env.getProperty(propSubstr + ".key");
        String secretKey = env.getProperty(propSubstr + ".secretkey");
        String passphrase = env.getProperty(propSubstr + ".passphrase");
        exchConfig = new ExchConfig(apiKey, secretKey, passphrase);
    }

}
