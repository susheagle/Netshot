/**
 * Copyright 2013-2025 Netshot
 * 
 * This file is part of Netshot project.
 * 
 * Netshot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Netshot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Netshot.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.netshot.netshot.device;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;
import net.netshot.netshot.device.credentials.DeviceCredentialSet;
import net.netshot.netshot.rest.RestViews.DefaultView;

import org.hibernate.annotations.NaturalId;

/**
 * A domain identifies a part of the network managed from the same IP address.
 */
@Entity
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.NONE)
public class Domain {

	/** The id. */
	@Getter(onMethod=@__({
		@Id, @GeneratedValue(strategy = GenerationType.IDENTITY),
		@XmlAttribute, @JsonView(DefaultView.class)
	}))
	@Setter
	private long id;

	/** The change date. */
	@Getter(onMethod=@__({
		@XmlElement, @JsonView(DefaultView.class)
	}))
	@Setter
	private Date changeDate;
	
	@Getter(onMethod=@__({
		@Version
	}))
	@Setter
	private int version;

	/** The name. */
	@Getter(onMethod=@__({
		@NaturalId(mutable = true),
		@XmlElement, @JsonView(DefaultView.class)
	}))
	@Setter
	private String name;

	/** The description. */
	@Getter(onMethod=@__({
		@XmlElement, @JsonView(DefaultView.class)
	}))
	@Setter
	private String description;

	/** The domain credential sets. */
	@Getter(onMethod=@__({
		@OneToMany(mappedBy = "mgmtDomain")
	}))
	@Setter
	private Set<DeviceCredentialSet> credentialSets = new HashSet<>();

	/** The server4 address. */
	@Getter(onMethod=@__({
		@XmlElement, @JsonView(DefaultView.class),
		@JsonSerialize(using = Network4Address.AddressOnlySerializer.class),
		@JsonDeserialize(using = Network4Address.AddressOnlyDeserializer.class),
		@AttributeOverrides({
			@AttributeOverride(name = "address", column = @Column(name = "ipv4_address")),
			@AttributeOverride(name = "prefixLength", column = @Column(name = "ipv4_pfxlen")),
			@AttributeOverride(name = "addressUsage", column = @Column(name = "ipv4_usage")) })
	}))
	@Setter
	private Network4Address server4Address;

	/** The server6 address. */
	@Getter(onMethod=@__({
		@XmlElement, @JsonView(DefaultView.class),
		@JsonSerialize(using = Network6Address.AddressOnlySerializer.class),
		@JsonDeserialize(using = Network6Address.AddressOnlyDeserializer.class),
		@AttributeOverrides({
			@AttributeOverride(name = "address1", column = @Column(name = "ipv6_address1")),
			@AttributeOverride(name = "address2", column = @Column(name = "ipv6_address2")),
			@AttributeOverride(name = "prefixLength", column = @Column(name = "ipv6_pfxlen")),
			@AttributeOverride(name = "addressUsage", column = @Column(name = "ipv6_usage")) })
	}))
	@Setter
	private Network6Address server6Address;

	/**
	 * Instantiates a new domain.
	 */
	protected Domain() {
		// Hibernate only
	}

	/**
	 * Instantiates a new domain.
	 * 
	 * @param name
	 *          the name
	 * @param description
	 *          the description
	 * @param server4Address
	 *          the server4 address
	 * @param server6Address
	 *          the server6 address
	 */
	public Domain(String name, String description,
			Network4Address server4Address, Network6Address server6Address) {
		this.name = name;
		this.description = description;
		this.server4Address = server4Address;
		this.server6Address = server6Address;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Domain)) {
			return false;
		}
		Domain other = (Domain) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		}
		else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Device Domain " + id + " (name '" + name + "')";
	}

}
