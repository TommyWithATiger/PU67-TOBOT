package data.reference;

import data.Topic;
import data.dao.ReferenceDAO;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.apache.commons.validator.routines.UrlValidator;

@Entity
@NamedQueries({
    @NamedQuery(name = "findAllReferences", query = "SELECT r FROM Reference r"),
    @NamedQuery(name = "findReferencesByTitle", query = "SELECT r FROM Reference r WHERE r.title LIKE CONCAT('%', :title, '%')")
})
@Table
public class Reference {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String title;
  private String description;
  private String link;

  @Enumerated(value = EnumType.STRING)
  @Convert(converter = ReferenceTypeConverter.class)
  private ReferenceType referenceType;

  @ManyToMany
  @JoinTable(name = "REFERENCE_TAGS")
  private Collection<Topic> tags;

  public Reference() {
    super();
    tags = new ArrayList<>();
  }

  public Reference(String title, String description, String link, ReferenceType referenceType)
      throws MalformedURLException {
    this();
    setTitle(title);
    setDescription(description);
    setReferenceType(referenceType);
    setLink(link);
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) throws MalformedURLException {
    String[] schemes = {"http","https"};
    UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_2_SLASHES);
    if (link.matches("(https?:\\/\\/(?:www\\.|(?!www))[^\\s\\.]+\\.[^\\s]{2,}|www\\.[^\\s]+\\.[^\\s]{2,})")) {
    //if (urlValidator.isValid(link)) {
      this.link = link;
    }
    else throw new MalformedURLException();
    //TODO different validation based on what type of link it is (youtube etc.)
  }

  public ReferenceType getReferenceType() {
    return referenceType;
  }

  public void setReferenceType(ReferenceType referenceType) {
    this.referenceType = referenceType;
  }

  /***
   *
   * @return unmodifiable copy of the tag collection
   */
  public Collection<Topic> getTags() {
    return Collections.unmodifiableCollection(tags);
  }

  public boolean hasTag(Topic topic) {
    return tags.contains(topic);
  }

  public void addTag(Topic tag) {
    if (!tags.contains(tag)) {
      tags.add(tag);
    }
  }

  public void addTags(Collection<Topic> newTags) {
    newTags.forEach(this::addTag);
  }

  /**
   * Adds the Reference to the database
   */
  public void create() {
    ReferenceDAO.getInstance().persist(this);
  }

  /**
   * Removes the Reference from the database
   */
  public void delete() {
    ReferenceDAO.getInstance().remove(this);
  }

  /**
   * Updates the Reference's database entry
   */
  public void update() {
    ReferenceDAO.getInstance().merge(this);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Reference reference = (Reference) o;

    return id == reference.id;
  }

  @Override
  public int hashCode() {
    return id;
  }
}
