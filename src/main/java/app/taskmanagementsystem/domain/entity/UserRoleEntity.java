package app.taskmanagementsystem.domain.entity;



import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "roles")
public class UserRoleEntity extends BaseEntity {

    @Column(name = "role", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleTypeEnum role;
    @Column(name = "description", nullable = false)
    private String description;

    // TODO: 3/20/2023 da mahna eager
    @ManyToMany(mappedBy = "roles", targetEntity = UserEntity.class)
    private List<UserEntity> users;

    public UserRoleEntity() {
    }

    public RoleTypeEnum getRole() {
        return role;
    }

    public UserRoleEntity setRole(RoleTypeEnum role) {
        this.role = role;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UserRoleEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public UserRoleEntity setUsers(List<UserEntity> users) {
        this.users = users;
        return this;
    }

    // TODO: 3/17/2023 it is clear method for deleting form many to many
    public void removeRoleFromAllUsers(UserRoleEntity roleToDel) {
        if (roleToDel == null) {
            return;
        }
        this.users
                .forEach(userEntity -> userEntity.getRoles().remove(roleToDel));
    }
}
