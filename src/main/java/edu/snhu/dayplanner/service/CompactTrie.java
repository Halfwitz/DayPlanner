package edu.snhu.dayplanner.service;

import java.util.*;

/**
 * A data structure for efficient information retrieval using the values of each attribute of inserted entities.
 * This trie supports insertion, deletion, and searching of entities using the attribute  values that
 * should be returned by {@code T.getFieldValue(F)} for each supplied field.
 *
 * @param <T> The type of entity stored in this trie
 * @param <F> The enum type defining fields in the entity, used to retrieve attributes.
 */
public class CompactTrie<T extends Entity<F>, F extends Enum<F>> {
    private final List<F> fields;
    private final CompactTrieNode<T, F> root;
    private boolean isCaseSensitive;

    /**
     * Initializes an empty trie with a list of fields. By, default objects added to this trie will be indexed with
     * lowercase values, and searches will use lowercase.
     * @param fields list of enum values representing fields that can be indexed and searched.
     *               String values must be returned by {@code T.getFieldValue(F)}
     */
    public CompactTrie(List<F> fields) {
        this(fields, false);
    }

    /**
     * Initializes an empty trie with a list of fields, with a case sensititivity specified
     * @param fields list of enum values representing fields that can be indexed and searched.
     *               String values must be returned by {@code T.getFieldValue(F)}
     * @param isCaseSensitive sets if search results should be case-sensitive, if false, indexes added to this trie
     *                        are lowercase.
     */
    public CompactTrie(List<F> fields, boolean isCaseSensitive) {
        this.root = new CompactTrieNode<>("");
        this.fields = fields;
        this.isCaseSensitive = isCaseSensitive;

    }

    // PUBLIC METHODS
    /**
     * Inserts the supplied object into this trie. Each attribute found using the fields supplied to constructor
     * becomes a key used to index the entity for efficient lookup.
     */
    public void insert(T object) {
        for (F field : fields) {
            insert(object, field);
        }
    }

    /**
     * Update the trie to replace nodes/values of an oldAttribute with entity's new attributes.
     * Whenever an object in this trie changes attribute values, call this method to update any keys in this trie for
     * the associated field of that attribute.
     * @param object    The entity being updated (should have already been inserted, then changed)
     * @param field     The field of the entity being updated
     * @param oldValue  The old value of the field before the update (should be in this trie)
     */
    public void update(T object, F field, String oldValue) {
        delete(root, object, field, oldValue, 0, null);
        insert(object, field);
    }

    /**
     * Removes the entity from the trie for all fields
     * @param object The entity to remove
     */
    public void delete(T object) {
        for (F field : fields) {
            delete(root, object, field, object.getFieldValue(field), 0, null);
        }
    }

    /**
     * Searches for the first entity with attribute exactly matching the argument.
     * Return the first object with an attribute exactly matching arg regardless of field
     * @param arg   The attribute value to search
     * @return      The first matching entity or null if no match
     */
    public T search(String arg) {
        return search(arg, null);
    }

    /**
     * Searches for the first entity with attribute exactly matching the argument and the specified field type.
     * Return the first object with an attribute exactly matching arg regardless of field
     * @param arg   The attribute value to search
     * @param field The field to search within
     * @return      The first matching entity with a {@code field} field type, or null if no match
     */
    public T search(String arg, F field) {
        Set<T> result = searchAll(arg, field); // gets data from a single matching node
        return result == null ? null : result.stream().findFirst().orElse(null);
    }

    /**
     * Searches for all entities in the trie with attributes exactly matching arg
     *
     * @param arg   The attribute value to search for
     * @return      A set of all matching entities or null if no matches
     */
    public Set<T> searchAll(String arg) {
        return searchAll(arg, null);
    }

    /**
     * Searches for all entities in the trie with attributes exactly matching arg and associated with field
     * @param arg   The attribute value to search for
     * @param field The field type that the attribute must be associated with
     * @return      A set of all matching entities or null if no matches
     */
    public Set<T> searchAll(String arg, F field) {
        CompactTrieNode<T, F> resultNode = searchNode(arg);
        return resultNode == null ? null : resultNode.getData(field);
    }

    /**
     * Searches for all entities with an attribute of the specified field starting with a given prefix
     * @param prefix    The prefix to search for
     * @param field     The field to search within
     * @return          A set of entities with attributes of the field type starting with the prefix
     */
    public Set<T> searchAllWithPrefix(String prefix, F field) {
        Set<T> result = new HashSet<>();

        searchAllWithPrefix(prefix, field, result);
        return result;
    }

    /**
     * Searches for all entities with an attribute starting with a given prefix
     * @param prefix    The prefix to search for
     * @return          A set of entities with attributes starting with the prefix
     */
    public Set<T> searchAllWithPrefix(String prefix) {
        Set<T> result = new HashSet<>();

        searchAllWithPrefix(prefix, null, result);
        return result;
    }

    /**
     * Recursively prints each node of the tree to visualize each node's children
     */
    public void printChildren() {
        printChildren(root, 0);
    }

    // PRIVATE METHODS
    // search algorithm retrieving the data associated with a full word key in this trie, or null
    private CompactTrieNode<T, F> searchNode(String word) {
        // iterate nodes until finding the node representing the word or there are no more nodes that match word[i:end]
        CompactTrieNode<T, F> node = root;
        int i = 0;
        while (i < word.length()) {
            // use the first letter of the word[i:end] substring as key to find the next node
            // if i=0 and word is "Michael", use "M" to find the next node (which may be any string starting with "M")
            Character key = word.charAt(i);
            CompactTrieNode<T, F> childNode = node.children.get(key);

            // TERMINATE If a node wasn't found for the key, there are no nodes that represent the word
            if (childNode == null) {
                return null;
            }

            node = childNode;   // If a child is linked to the key, iterate this node to the child

            /* Now compare this node's partial prefix and the substring of word[i:end].
            Depending on the shared prefix of these two...
             - the node CONTAINS WORD PORTION If the shared # of chars is the same as this node's partial prefix.
               - this node represents TARGET WORD if i + # of shared chars is same length as the target word.
             - NO MATCH If the shared # of chars differs, it means this node is longer than the word
              for example if the word is "Alex" and this node is "Alexander", there can be no "Alex" beyond this node.*/
            int commonPrefixLen = commonPrefixLength(node.prefixPartial, word.substring(i));
            if (commonPrefixLen != node.prefixPartial.length()) { return null; } // NO MATCH
            // NODE CONTAINS WORD PORTION
            i+= commonPrefixLen;
            if (i == word.length()) { // NODE REPRESENTS TARGET WORD
                // return data if this is an end node.
                return node.isWordEnd? node : null;
            }
        }
        return null; //  no node exists with length of word
    }

    // used for public prefix search method to add all search matches to the supplied result
    // Traverses the list until reaching a node that represents a word starting with the entire prefix,
    // Then traverses each subtree of the node, adding any data to result.
    private void searchAllWithPrefix(String prefix, F field, Set<T> results) {
        if (!isCaseSensitive) {
            prefix = prefix.toLowerCase();
        }
        // Iterate nodes until finding the node that represents a word starting with the prefix.
        CompactTrieNode<T, F> node = root;
        int i = 0;
        while (i < prefix.length()) {
            // use the first letter of the word[i:end] substring as key to find the next node
            // if i=0 and word is "Michael", use "M" to find the next node (which may be any string starting with "M")
            Character key = prefix.charAt(i);
            CompactTrieNode<T, F> childNode = node.children.get(key);
            if (childNode == null) {  // TERMINATE If a node wasn't found for the key, there are no nodes for the prefix
                return;
            }
            node = childNode; // If a child is linked to the key, iterate this node to the child

            /* current node represents prefix[0:i]+partial, we need to check if partial starts with prefix[i:end]
            - This node MAY CONTAIN prefix if this node's partial is longer than prefix[i:end]
              - This node CONTAINS prefix if this node starts with prefix[i:end]
              - NO MATCHES if this node does not start with prefix
            - This node MAY CONTAIN PORTION of prefix if prefix[i:end] is longer than node portion
              - ITERATE if the remaining prefix starts with this node's portion, need to find node with full prefix
              - NO MATCHES if remaining prefix doesn't start with this nodes partial.

            - if this node is shorter than the substring */
            String remaining = prefix.substring(i); // remainder of prefix to check if this node partial starts with
            if (node.prefixPartial.length() > remaining.length()) { // MAY CONTAIN PREFIX
                if (node.prefixPartial.startsWith(remaining)) {
                    break; // CONTAINS PREFIX - EXIT LOOP
                } else {
                    return; // NO MATCHES - terminate
                }
            } else { // MAY CONTAIN PORTION of prefix
                if (remaining.startsWith(node.prefixPartial)) {
                    i += node.prefixPartial.length(); // ITERATE remainder of prefix
                } else {
                    return; // NO MATCHES - terminate
                }
            }
        }

        // At this point the loop has been iterated until this node starts with the target prefix. All descendents of
        // this node is a match for the search prefix and should be added to the set
        addAllFrom(node, field, results);
    }

    // removes an objects attribute associated with the field from the trie as a key.
    // if multiple objects have matching attributes, it simply deletes the values from the leaf node's data
    private boolean delete(CompactTrieNode<T, F> node, T object, F field, String word, int index, CompactTrieNode<T, F> parentNode) {
        if (!isCaseSensitive) {
            word = word.toLowerCase();
        }
        //  The target word has been traversed and if it is an end, removal should be handled
        if (index == word.length()) {
            // If this node is a word end, remove target data
            if (node.isWordEnd) {
                node.removeObject(field, object);

                // If this node still has data, don't delete it.
                if (node.data != null && !node.data.isEmpty()) {
                    return false;
                }

                // If node has no more data, it is no longer a word End
                node.isWordEnd = false;

                // If the node has children, it cannot be deleted.
                return node.children.isEmpty();
            }
            return false; // the target word does not exist as a key in this trie
        }

        // traverse until target word is found and delete if marked for deletion, merging single branches upwards
        Character key = word.charAt(index);
        CompactTrieNode<T, F> childNode = node.children.get(key); // finds child node with partial word starting with key

        if (childNode == null) { return false;} // no values exist with the specified prefix, nothing to delete

        int commonPrefixLen = commonPrefixLength(childNode.prefixPartial, word.substring(index));
        if (commonPrefixLen != childNode.prefixPartial.length()) { // word does not exist if
            return false;
        }

        boolean deleteChild = // recursively traverse until a target node if found, true if it should be deleted.
                delete(childNode, object, field, word, index + commonPrefixLen, node);

        // Merge any values with this node if necessary, and remove reference to the deleted node
        if (deleteChild) {
            System.out.println("Deleting node: " + childNode);
            node.children.remove(key);// remove the child node reference from this node

            if (node.isWordEnd) { return false; } //  don't merge if this node acts as a word end

            // If this node now has only one child and is not a word end, merge child up.
            if (node.children.size() == 1) {
                CompactTrieNode<T, F> child = node.children.entrySet().iterator().next().getValue();

                // merge child into current node
                node.prefixPartial += child.prefixPartial;
                node.children = child.children;
                node.isWordEnd = child.isWordEnd;
                node.data = child.data;
            }
        }
        return node.children.isEmpty() && !node.isWordEnd; // delete this node if empty
    }


    // Insertion algorithm that adds nodes associated with the word found by this objects field attribute, with the
    // object being stored in the final leaf node, associated with the field.
    private void insert(T object, F field) {
        String word = object.getFieldValue(field); // the key that will be used to retrieve this object
        if (!isCaseSensitive) word = word.toLowerCase();
        System.out.println("Inserting: " + word + " as " + field);
        CompactTrieNode<T, F> node = root;

        // insert this object by iterating each node based on the substring of the full word
        int i = 0;
        while (i < word.length()) {
            Character key = word.charAt(i); // char used to find the next node.
            CompactTrieNode<T, F> childNode = node.children.get(key); // finds child node with partial word starting with key

            // CASE 1: this node doesn't have a child node found with the key.
            // create a child node storing the remainder of the word if this node doesn't have a child found with key.
            if (childNode == null) {
                System.out.println("   Child not found for key-" + key + ". Creating new child for parent-" + node + ": ");
                CompactTrieNode<T, F> newNode = new CompactTrieNode<>(word.substring(i));
                newNode.isWordEnd = true;
                newNode.addObject(object, field); // because new node is a word ending node it contains the object/field.
                node.children.put(key, newNode); // point to new node using key in this node's children.
                System.out.println("      Child " + newNode + " added to parent " + node + " for key: " + key);
                break; // INSERTION COMPLETE
            }

            // CASE 2: this node has a child node found with the key
            // swap this node to the child node and compare length of its partial prefix value and the remainder of the word [i:end]
            System.out.println("    Swapping to child found for " + key + ": " + childNode);
            node = childNode;
            int commonPrefixLen = commonPrefixLength(node.prefixPartial, word.substring(i));
            i+= commonPrefixLen; // skip all prefix letters shared with this node in the iteration

            // CASE 2a: the current node's prefix partial has remaining letters not shared by word[i:end]
            // split this node into two with the first (parent) containing letters shared by word[i:end], and the second
            // (child) containing this node's unique letters and values
            if (commonPrefixLen < node.prefixPartial.length()) {
                System.out.println("    Common prefix for node (\"" + node.prefixPartial + "\") and target \"" + word + "\" is smaller than node's prefix. Splitting node.");
                // new child node copies this node's existing children and data
                CompactTrieNode<T, F> newChildNode = new CompactTrieNode<>(node.prefixPartial.substring(commonPrefixLen));
                newChildNode.isWordEnd = node.isWordEnd;
                newChildNode.children = node.children;
                newChildNode.data = node.data; // copy reference to associated field/object map

                // replace the attributes of this node with new substring and data,add newChildNode as child
                node.data = null; // dereference this node's data
                node.prefixPartial = node.prefixPartial.substring(0, commonPrefixLen);
                node.children = new HashMap<>();
                node.children.put(newChildNode.prefixPartial.charAt(0), newChildNode); // reference child with first char

                System.out.println("        Child" + newChildNode + " added for " + node + "with key: " + newChildNode.prefixPartial.charAt(0));

                // this node is the end of the word we are inserting if it's length matches the inserted word, data should be inserted if true.
                node.isWordEnd = (i == word.length());
                if (node.isWordEnd) {
                    System.out.println("        Word end found for " + word + ". Adding data to node...");
                    node.addObject(object, field);
                    System.out.println("            Data added: " + node);
                }
            } else if (commonPrefixLen == node.prefixPartial.length() && i == word.length()) { // this node is the end of the word, add data
                System.out.println("        Word end found for " + word + ". Adding data to node...");
                node.addObject(object, field);
                System.out.println("            Data added: " + node);
            }
            // CASE 2b: there are remaining letters of the word we are inserting, looping again will create the new node
            // for the remainder of our word.
        }
    }

    // HELPER METHODS //
    /**
     * Returns the length of the longest prefix shared by two words
     * "Abroad" and "Abraham" have a shared prefix length of 3 ("Abr")
     * @param str1 first string
     * @param str2 second string
     * @return length of the longest shared prefix.
     */
    public int commonPrefixLength(String str1, String str2) {
        int length = 0;
        while (length < Math.min(str1.length(), str2.length())
                && str1.charAt(length) == str2.charAt(length)) {
            length++;
        }
        return length;
    }

    /**
     * @see #printChildren()
     */
    private void printChildren(CompactTrieNode<T, F> node, int index) {
        for (int i = index; i > 0; i--) {
            System.out.print("|  ");
        }
        System.out.print(node + "\n");
        index++;
        for (CompactTrieNode<T, F> entry : node.children.values()) {
            printChildren(entry, index);
        }
    }

    // helper method to add all objects from this current node and its descendents to the set, filtered by field type
    private void addAllFrom(CompactTrieNode<T, F> node, F fieldType, Set<T> results) {
        Stack<CompactTrieNode<T, F>> stack = new Stack<>();
        stack.push(node);
        // Pushes each node's children to a stack to be iterated through until traversing all descendents
        // any node containing data is added to the set
        while (!stack.isEmpty()) {
            CompactTrieNode<T, F> currNode = stack.pop();
            // If the current node is a word end, add its data for the given field if specified, or all fields
            if (currNode.isWordEnd && currNode.data != null) {
                Set<T> nodeData = currNode.getData(fieldType);
                if (nodeData != null) {
                    results.addAll(nodeData);
                }
            }
            // push current node's children to the stack to process
            for (CompactTrieNode<T, F> childNode : currNode.children.values()) {
                stack.push(childNode);
            }
        }
    }
}
