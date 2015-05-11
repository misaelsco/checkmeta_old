package br.com.correiam.checkmeta.dominio;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Misael Correia on 01/05/15.
 * misaelsco@gmail.com
 */
public class Meta {

    private Long id;
    private String name;
    private String description;
    private String dueDate;
    private String state;
    private String actualDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meta)) return false;

        Meta meta = (Meta) o;

        if (actualDate != null ? !actualDate.equals(meta.actualDate) : meta.actualDate != null)
            return false;
        if (description != null ? !description.equals(meta.description) : meta.description != null)
            return false;
        if (dueDate != null ? !dueDate.equals(meta.dueDate) : meta.dueDate != null) return false;
        if (id != null ? !id.equals(meta.id) : meta.id != null) return false;
        if (name != null ? !name.equals(meta.name) : meta.name != null) return false;
        if (state != null ? !state.equals(meta.state) : meta.state != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dueDate != null ? dueDate.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (actualDate != null ? actualDate.hashCode() : 0);
        return result;
    }
}
