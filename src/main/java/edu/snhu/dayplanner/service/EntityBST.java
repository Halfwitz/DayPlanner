package edu.snhu.dayplanner.service;

import java.util.*;

public class EntityBST<T extends Entity<F>, F extends Enum<F>> {
    EntityNode<T, F> root;
    List<F> fields;

    public EntityBST(List<F> fields) {
        this.fields = fields;
        root = null;
    }

    public void insert(final T entity) {
        for (final F field : fields) {
            root = insertNode(root, entity, field);
        }
    }

    public void update(T entity, F field, String oldValue) {
        root = deleteNode(root, entity, field, oldValue);
        root = insertNode(root, entity, field);
    }

    private EntityNode<T, F> insertNode(EntityNode<T, F> root, T entity, F fieldType) {
        if (root == null) {
            root = new EntityNode<>(entity, fieldType);
            return root;
        }
        String target = entity.getFieldValue(fieldType);
        int compare = target.compareToIgnoreCase(root.key);
        if (entity.getFieldValue(fieldType).compareToIgnoreCase(root.key) <= 0) {
            root.left = insertNode(root.left, entity, fieldType);
        } else if (entity.getFieldValue(fieldType).compareToIgnoreCase(root.key) > 0) {
            root.right = insertNode(root.right, entity, fieldType);
        }

        return root;
    }

    public void delete(final T entity) {
        for (final F field : fields) {
            deleteNode(root, entity, field);
        }
    }

    private EntityNode<T,F> deleteNode(EntityNode<T, F> root, T entity, F fieldType) {
        String targetKey = entity.getFieldValue(fieldType);
        return deleteNode(root, entity, fieldType, targetKey);
    }


    private EntityNode<T,F> deleteNode(EntityNode<T, F> root, T entity, F fieldType, String targetKey) {
        if (root == null) { // terminating case
            return null;
        }

        // compare difference between the entity's target key and this root node's key, search until they match
        if (targetKey.compareToIgnoreCase(root.key) < 0) {
            root.left = deleteNode(root.left, entity, fieldType, targetKey);
        } else if (targetKey.compareToIgnoreCase(root.key) > 0) {
            root.right = deleteNode(root.right, entity, fieldType, targetKey);
        } else { // Node matches key
            // must also check for matching fieldType and entity for a complete match
            if (root.fieldType == fieldType && root.value == entity) { // complete match
                // return the nodes child if it only has one child to replace this node
                if (root.left == null) {
                    return root.right;
                } else if (root.right == null) {
                    return root.left;
                }

                // if node has two children, move the smallest node in the right subtree as the successor
                EntityNode<T, F> successor = minNode(root.right);
                // replace this node with successor data and delete the successor
                root.key = successor.key;
                root.value = successor.value;
                root.fieldType = successor.fieldType;
                root.right = deleteNode(root.right, successor.value, successor.fieldType, successor.key);

            } else { // not complete match, check if next node matches (must be on left side because == current key)
                root.left = deleteNode(root.left, entity, fieldType, targetKey);
            }
        }
        return root;
    }

    /**
     * Return first matching Entity with an attribute exactly matching key
     * @param key
     * @return
     */
    public T find(String key) {
        EntityNode<T, F> node = root;

        while (node != null) {
            if (key.compareToIgnoreCase(node.key) < 0) {
                node = node.left;
            } else if (key.compareToIgnoreCase(node.key) > 0) {
                node = node.right;
            } else {
                return node.value;
            }
        }
        // match could not be found
        return null;
    }

    /**
     * Return first matching Entity where the attribute of the specified fieldType matches exactly matches the key
     * @param key
     * @param fieldType
     * @return
     */
    public T find(String key, F fieldType) {
        EntityNode<T, F> node = root;

        while (node != null) {
            if (key.compareToIgnoreCase(node.key) < 0) { // target key should be in left subtree
                node =  node.left;
            } else if (key.compareToIgnoreCase(node.key) > 0) { // target should be in right subtree
                node = node.right;
            } else { // keys match
                if (fieldType == node.fieldType) {
                    return node.value;
                } else { // if keys don't match, keep traversing left until null
                    node = node.left;
                }
            }
        }
        // match could not be found
        return null;
    }

    /**
     * Return first matching Entity if any attribute starts with the prefix key
     * @param prefixKey
     * @return
     */
    public T findStartingWith(String prefixKey) {
        EntityNode<T, F> result = findNodeStartingWith(prefixKey, null, root);
        if (result != null) {
            return result.value;
        }
        return null; // no result        EntityNode<T, F> node = root;
    }

    /**
     *      * Return first matching Entity where the attribute of the specified fieldType starts with the prefix key.
     * @param prefixKey
     * @param fieldType
     * @return
     */
    public T findStartingWith(String prefixKey, F fieldType) {
        EntityNode<T, F> result = findNodeStartingWith(prefixKey, fieldType, root);
        if (result != null) {
            return result.value;
        }
        return null; // no result
    }

    private EntityNode<T, F> findNodeStartingWith(String prefixKey, F fieldType, EntityNode<T, F> root) {
        EntityNode<T, F> node = root;

        while (node != null) {
            // current node is a match if keys match and fieldType matches (if specified)
            if ((fieldType == null || node.fieldType == fieldType) && node.key.startsWith(prefixKey)) {
                return node;
            }
            // not a match, traverse the correct subtree
            if (prefixKey.compareToIgnoreCase(node.key) <= 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        // match could not be found
        return null;
    }

    /**
     * Return all entities with an attribute that starts with the prefixKey
     * (Entity with a "Johnathon" attribute is returned if prefixKey = "John")
     * @param prefixKey
     * @return
     */
    public Set<T> findAllStartingWith(String prefixKey) {
        return findAllStartingWith(prefixKey, null);
    }

    private void findAllStartingWith(String prefixKey, F fieldType, EntityNode<T, F> root, Set<T> matches) {
        if (root == null) {
            return;
        }
        EntityNode<T, F> result = findNodeStartingWith(prefixKey, fieldType, root);
        if (result != null) {
            matches.add(result.value);
            findAllStartingWith(prefixKey, fieldType, result.left, matches);
            findAllStartingWith(prefixKey, fieldType, result.right, matches);
        }
    }

    /**
     * Return all entities with an attribute starting with prefixKey associated with the specified field
     * @param prefixKey
     * @param fieldType
     * @return
     */
    public Set<T> findAllStartingWith(String prefixKey, F fieldType) {
        Set<T> matches = new HashSet<>();
        EntityNode<T, F> firstMatch = findNodeStartingWith(prefixKey, fieldType, root);
        if (firstMatch != null) {
            findAllStartingWith(prefixKey, fieldType, root, matches);
        }

        return matches;
    }

    private EntityNode<T, F> minNode(EntityNode<T, F> root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    public void printAll() {
        if (root == null) {
            System.out.println("Empty Tree");
            return;
        }

        // Queue for BFS
        Queue<EntityNode<T, F>> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            // Number of nodes in the current level
            int levelSize = queue.size();

            // Print all nodes in this level
            for (int i = 0; i < levelSize; i++) {
                EntityNode<T, F> current = queue.poll();
                // Print key in quotes
                System.out.print("\"" + current.key + "\" ");

                // Enqueue children
                if (current.left != null)  queue.offer(current.left);
                if (current.right != null) queue.offer(current.right);
            }

            // New line after finishing the current level
            System.out.println();
        }
    }
}
