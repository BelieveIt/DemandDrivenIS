package test;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "documentService")
@ApplicationScoped
public class DocumentService {

    public TreeNode createDocuments() {
        TreeNode root = new DefaultTreeNode(new Document("Files", "-", "Folder"), null);

        TreeNode documents = new DefaultTreeNode(new Document("Documents", "-", "Folder"), root);
        new DefaultTreeNode(new Document("Pictures", "-", "Folder"), root);
        new DefaultTreeNode(new Document("Movies", "-", "Folder"), root);

        new DefaultTreeNode(new Document("Work", "-", "Folder"), documents);
        new DefaultTreeNode(new Document("PrimeFaces", "-", "Folder"), documents);
        return root;
    }
}