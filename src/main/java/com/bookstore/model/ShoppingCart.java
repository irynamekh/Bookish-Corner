package com.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@SQLDelete(sql = "UPDATE shopping_cart SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;
    @OneToMany(mappedBy = "shoppingCart")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CartItem> cartItems = new HashSet<>();
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;
}
