package dbService.dataSets;

import javax.persistence.*;

@Entity
@Table(name = "poms")
public class PomDataSet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "head")
    private String head;

    @Column(name = "project_attributes")
    private String projectAttributes;

    @Column(name = "model_version")
    private String modelVersion;

    @Column(name = "other_code")
    private String otherCode;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "pomDataSet")
    private GavDataSet gavDataSet;

    public PomDataSet() {}

    public PomDataSet(String head, String projectAttributes, String modelVersion, String otherCode) {
        this.setHead(head);
        this.setProjectAttributes(projectAttributes);
        this.setModelVersion(modelVersion);
        this.setOtherCode(otherCode);
    }

    public PomDataSet(String head, String projectAttributes, String modelVersion, String otherCode, GavDataSet gavDataSet) {
        this.setHead(head);
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

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
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
}
