package edu.snhu.dayplanner.ui;

import edu.snhu.dayplanner.service.Entity;
import javafx.scene.Node;
import java.util.function.BiConsumer;

/**
 * Creates an instance of V
 * @param <T> Subclass of Entity
 * @param <F> Fields enumeration contained within Entity class
 * @param <V> Subclass of EntityView used to create the user interface
 */
@FunctionalInterface
public interface EntityViewFactory<T extends Entity<F>, F extends Enum<F>, V extends EntityView<T, F>>
{
    /**
     * Create an EntityView subclass instance
     * @param onRemove  The remove handler for removing a data table row
     * @param onEdit    The edit handler for editing a field in a row.
     * @return An instance of the EntityView subclass
     */
    V createView(BiConsumer<T, Node> onRemove,
                 TriConsumer<T, F, Node> onEdit);
}
