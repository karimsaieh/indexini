package tn.insat.pfe.searchservice.mq.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class LdaTopicsDescriptionPayload {
    @JsonProperty("id")
    private String id;
    @JsonProperty("description")
    private List<String> description;

    public Map toMap() {
        return new ObjectMapper().convertValue(this, Map.class);
    }

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
