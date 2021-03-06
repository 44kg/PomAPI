package dbService.dataSets;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "gavs")
public class GavDataSet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "groupId")
    private String groupId;

    @Column(name = "artifactId")
    private String artifactId;

    @Column(name = "version")
    private String version;

    @OneToOne
    @JoinColumn(name = "pom_id", referencedColumnName = "id")
    private PomDataSet pomDataSet;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "dependencies", joinColumns = @JoinColumn(name = "main_gav"),
            inverseJoinColumns = @JoinColumn(name = "dependent_gav"))
    private Set<GavDataSet> dependentGavs = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "dependencies", joinColumns = @JoinColumn(name = "dependent_gav"),
            inverseJoinColumns = @JoinColumn(name = "main_gav"))
    private Set<GavDataSet> mainGavs = new HashSet<>();

    public GavDataSet() {}

    public GavDataSet(String groupId, String artifactId, String version) {
        this.setGroupId(groupId);
        this.setArtifactId(artifactId);
        this.setVersion(version);
    }

    public GavDataSet(String groupId, String artifactId, String version, PomDataSet pomDataSet, Set<GavDataSet> gavDataSets) {
        this.setGroupId(groupId);
        this.setArtifactId(artifactId);
        this.setVersion(version);
        this.setPomDataSet(pomDataSet);
        this.setDependentGavs(gavDataSets);
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        map.put("artifactId", artifactId);
        map.put("version", version);
        return map;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public PomDataSet getPomDataSet() {
        return pomDataSet;
    }

    public void setPomDataSet(PomDataSet pomDataSet) {
        this.pomDataSet = pomDataSet;
    }

    public Set<GavDataSet> getDependentGavs() {
        return dependentGavs;
    }

    public void setDependentGavs(Set<GavDataSet> gavDataSets) {
        this.dependentGavs = gavDataSets;
    }

    public void addDependentGav(GavDataSet gavDataSet) {
        this.dependentGavs.add(gavDataSet);
    }

    public Set<GavDataSet> getMainGavs() {
        return mainGavs;
    }

    public void setMainGavs(Set<GavDataSet> mainGavs) {
        this.mainGavs = mainGavs;
    }

    public void addMainGav(GavDataSet gavDataSet) {
        this.mainGavs.add(gavDataSet);
    }

    @Override
    public String toString() {
        return  "GavDataSet={id=" + id + ", groupId='" + groupId + "', artifactId='" + artifactId + "', version='"
                + version + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GavDataSet that = (GavDataSet) o;
        if (!Objects.equals(groupId, that.groupId)) return false;
        if (!Objects.equals(artifactId, that.artifactId)) return false;
        if (!Objects.equals(version, that.version)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (artifactId != null ? artifactId.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
