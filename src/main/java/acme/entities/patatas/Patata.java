package acme.entities.patatas;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import acme.framework.datatypes.Money;
import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Patata extends DomainEntity {
	
    protected static final long    serialVersionUID    = 1L;
    
    @NotBlank
    @Column(unique = true)
    protected String patataTicker;
    
    // está implementado como momento futuro con respecto a la creación del shout (now)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
    protected Date patataMoment;
	
	@NotNull
	@Valid
	protected Money patataValue;
	
	@NotNull
	protected Boolean patataBoolean;


}
