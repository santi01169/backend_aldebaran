package com.app.inventario.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movimientos_stock")
public class MovimientoStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "\"fechaMovimiento\"")  // ✅ CON comillas escapadas y camelCase
    private LocalDateTime fechaMovimiento;

    @Column(name = "\"stockAntes\"")  // ✅ CON comillas escapadas y camelCase
    private Integer stockAntes;

    @Column(name = "\"stockDespues\"")  // ✅ CON comillas escapadas y camelCase
    private Integer stockDespues;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoMovimientoStock tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private StockEntity stock;
}
