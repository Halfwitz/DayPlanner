package edu.snhu.dayplanner.service;

import java.util.*;

public class CompactTrie<T extends Entity<F>, F extends Enum<F>> {
    private final List<F> fields;
    public CompactTrieNode<T, F> root;

    public CompactTrie(List<F> fields) {
        this.root = new CompactTrieNode<>("");
        this.fields = fields;

    }

    // PUBLIC METHODS
    /**
     * Inserts the supplied object into this trie. Each attribute found using the fields supplied to constructor
     * becomes a key used to look up this object efficiently.
     */
    public void insert(T object) {
        for (F field : fields) {
            insert(object, field);
        }
    }

    /**
     * Whenever an object in this trie changes attribute values, call this method to update any keys in this trie for
     * the associated field of that attribute.
     * @param object
     * @param field
     */
    public void update(T object, F field, String oldValue) {
        delete(root, object, field, oldValue, 0, null);
        insert(object, field);
    }

    // removes all the attributes of an object from this trie (gets value for each field and removes them as keys
    public void delete(T object) {
        for (F field : fields) {
            delete(root, object, field, object.getFieldValue(field), 0, null);
        }
    }

    /**
     * Return the first object with an attribute exactly matching arg regardless of field
     * (Note Matches are unordered)
     * @param arg
     * @return
     */
    public T search(String arg) {
        return search(arg, null);
    }

    /**
     * Return the first object with an attribute exactly matching arg associated with the specified field
     * (Note matches are unordered)
     * @param arg
     * @param field
     * @return
     */
    public T search(String arg, F field) {
        Set<T> result = searchAll(arg, field);
        return result == null ? null : result.stream().findFirst().orElse(null);

    }

    // return all entities in the trie with attributes exactly matching arg
    public Set<T> searchAll(String arg) {
        return searchAll(arg, null);
    }

    // return all entities in the trie with attributes exactly matching arg only if they are associated with field
    public Set<T> searchAll(String arg, F field) {
        CompactTrieNode<T, F> resultNode = searchNode(arg);
        return resultNode == null ? null : resultNode.getData(field);
    }

    public Set<T> searchAllWithPrefix(String prefix, F field) {
        Set<T> result = new HashSet<>();

        searchAllWithPrefix(prefix, field, result);
        return result;
    }

    private void searchAllWithPrefix(String prefix, F field, Set<T> result) {
        CompactTrieNode<T, F> node = root;
        int i = 0;
        // Iterate nodes until the current nodes prefix starts with the entire supplied arg.
        while (i < prefix.length()) {
            Character key = prefix.charAt(i); // char used to find the next node.
            CompactTrieNode<T, F> childNode = node.children.get(key); // finds child node with partial word starting with key
            // CASE 1: this node doesn't have a child node found with the key, meaning no matching words exist in the trie
            if (childNode == null) {
                return;
            }
            node = childNode;
            String remaining = prefix.substring(i);
            // Case 1: The node’s partial is longer than the remaining search string.
            if (node.prefixPartial.length() > remaining.length()) {
                // If the node’s text starts with the remaining prefix, we’ve matched.
                if (node.prefixPartial.startsWith(remaining)) {
                    // Set i to prefix.length() to indicate a full match.
                    i = prefix.length();
                    break;
                } else {
                    return;
                }
            }
            // Case 2: The node’s partial is shorter than or equal to the remaining search string.
            else {
                // If the remaining search string starts with the node’s partial, continue.
                if (remaining.startsWith(node.prefixPartial)) {
                    i += node.prefixPartial.length();
                } else {
                    return;
                }
            }
        }

        // each node from this point has a prefix matching the supplied prefix and can be added to the set
        Stack<CompactTrieNode<T, F>> stack = new Stack<>();
        stack.push(node);
        while (!stack.isEmpty()) {
            CompactTrieNode<T, F> currNode = stack.pop();
            // If the current node is a word end, add its data for the given field if specified, or all fields
            if (currNode.isWordEnd && currNode.data != null) {
                Set<T> nodeData = currNode.getData(field);
                if (nodeData != null) {
                    result.addAll(nodeData);
                }
            }
            // push current node's children to the stack to process
            for (CompactTrieNode<T, F> childNode : currNode.children.values()) {
                stack.push(childNode);
            }

        }
    }


    // search algorithm retrieving the data associated with a full word key in this trie, or null
    private CompactTrieNode<T, F> searchNode(String word) {
        System.out.println("Searching: " + word);
        CompactTrieNode<T, F> node = root;
        int i = 0;
        while (i < word.length()) {
            Character key = word.charAt(i); // char used to find the next node.
            CompactTrieNode<T, F> childNode = node.children.get(key); // finds child node with partial word starting with key
            // CASE 1: this node doesn't have a child node found with the key, meaning no matching words exist in the trie
            if (childNode == null) {
                return null;
            }
            // CASE 2: this node has a child node found with the key
            // swap this node to the child node and compare length of its partial prefix value and the remainder of the word [i:end]
            node = childNode;
            int commonPrefixLen = commonPrefixLength(node.prefixPartial, word.substring(i));

            // if this node's partial prefix is not same length as the common prefix, no matching words exist in the trie
            // for example if word substring = "Alex" and this node is "Alexander", there is no "Alex" in the trie.
            if (commonPrefixLen != node.prefixPartial.length()) { return null; }

            // skip all prefix letters shared with this node in the iteration, if we are at word[end:] then check if this is a word end node and return
            i+= commonPrefixLen;
            if (i == word.length()) {
                // return data if this is an end node.
                return node.isWordEnd? node : null;
            }
        }
        return null;
    }

    // removes an objects attribute associated with the field from the trie as a key.
    // if multiple objects have matching attributes, it simply deletes the values from the leaf node's data
    private boolean delete(CompactTrieNode<T, F> node, T object, F field, String word, int index, CompactTrieNode<T, F> parentNode) {

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
                System.out.println("    Common prefix between " + node.prefixPartial + "\" and \"" + word + "\" is smaller than node's prefix length. Splitting node.");
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

    public void printChildren(CompactTrieNode<T, F> node) {
        System.out.println(node + " Children: ");
        for (CompactTrieNode<T, F> entry : node.children.values()) {
            printChildren(entry);
        }
    }
}
