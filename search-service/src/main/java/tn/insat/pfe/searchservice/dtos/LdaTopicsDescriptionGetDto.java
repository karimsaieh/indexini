package tn.insat.pfe.searchservice.dtos;

import java.util.List;

public class LdaTopicsDescriptionGetDto {
    private String id;
    private List<String> description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }
}
