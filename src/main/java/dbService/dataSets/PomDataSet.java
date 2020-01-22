package dbService.dataSets;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "poms")
public class PomDataSet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "project_attributes")
    private String projectAttributes;

    @Column(name = "model_version")
    private String modelVersion;

    @Type(type = "text")
    @Column(name = "other_code")
    private String otherCode;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "pomDataSet")
    private GavDataSet gavDataSet;

    public PomDataSet() {}

    public PomDataSet(String projectAttributes, String modelVersion, String otherCode) {
        this.setProjectAttributes(projectAttributes);
        this.setModelVersion(modelVersion);
        this.setOtherCode(otherCode);
    }

    public PomDataSet(String projectAttributes, String modelVersion, String otherCode, GavDataSet gavDataSet) {
        this.setProjectAttributes(projectAttributes);
        this.setModelVersion(modelVersion);
        this.setOtherCode(otherCode);
        gavDataSet.setPomDataSet(this);
        this.setGavDataSet(gavDataSet);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProjectAttributes() {
        return projectAttributes;
    }

    public void setProjectAttributes(String projectAttributes) {
        this.projectAttributes = projectAttributes;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getOtherCode() {
        return otherCode;
    }

    public void setOtherCode(String otherCode) {
        this.otherCode = otherCode;
    }

    public GavDataSet getGavDataSet() {
        return gavDataSet;
    }

    public void setGavDataSet(GavDataSet gavDataSet) {
        gavDataSet.setPomDataSet(this);
        this.gavDataSet = gavDataSet;
    }

    @Override
    public String toString() {
        return  "GavDataSet={id=" + id + ", projectAttributes.length='" + projectAttributes.length()
                + "', modelVersion='" + modelVersion + "', otherCode.length='" + otherCode.length() + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PomDataSet that = (PomDataSet) o;
        if (!Objects.equals(projectAttributes, that.projectAttributes)) return false;
        if (!Objects.equals(modelVersion, that.modelVersion)) return false;
        if (!Objects.equals(otherCode, that.otherCode)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = 31 * result + (projectAttributes != null ? projectAttributes.hashCode() : 0);
        result = 31 * result + (modelVersion != null ? modelVersion.hashCode() : 0);
        result = 31 * result + (otherCode != null ? otherCode.hashCode() : 0);
        return result;
    }
}
