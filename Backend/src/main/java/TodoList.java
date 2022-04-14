

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of named items on a To-Do list. The items are stored in
 * order. New items can only be added at the end. Existing items can be
 * completed. Completed items are removed after a little delay.
 * <p>
 * Think of this as a list of pairs (name, completed), where the latter is a
 * boolean value.
 */
public class TodoList {

  // AF: list with the given item names and completed = true if completedAt is
  //     a non-negative number
  private final List<String> items = new ArrayList<>();
  private final List<Long> completedAt = new ArrayList<>();
  private final int delaySec;

  // RI: delaySec is positive
  // RI: the two lists have the same length and contain no nulls

  // Note that the list may contain items that were completed more than
  // delaySec ago, which are no longer in the abstract state and should not be
  // shown. We could keep these forever and just not show them. However, we
  // will instead prune them out before returning the state to the client. (See
  // removeStaleItems below, which does this.)

  /**
   * Creates an empty to-do list where items are removed delaySec seconds after
   * they are completed.
   */
  public TodoList(int delaySec) {
    assert delaySec >= 0;
    this.delaySec = delaySec;
  }

  /**
   * @return A description of all the items, with each one on its own line and
   *     the text "\t(completed)" if it is completed.
   */
  public String describe() {
    removeStaleItems();  // NOTE: doesn't change abstract state

    StringBuilder buf = new StringBuilder();
    for (int i = 0; i < items.size(); i++) {
      if (i > 0)
        buf.append("\n");

      buf.append(items.get(i));
      if (completedAt.get(i) > 0)
        buf.append("\t(completed)");
    }
    return buf.toString();
  }

  /** @return Whether there is already an item with the given name. */
  public boolean has(String name) {
    return items.indexOf(name) >= 0;
  }

  /**
   * @requires There is not already an item with this name.
   * @modifies this
   * @effects Adds an uncompleted item with the given name (at the end).
   */
  public void add(String name) {
    assert name != null;
    assert !has(name);

    items.add(name);
    completedAt.add(-1L);
  }

  /**
   * @requires There is an item with this name.
   * @modifies this
   * @effects Marks the given item completed at this time.
   */
  public void completed(String name) {
    int index = items.indexOf(name);
    assert index >= 0; 

    completedAt.set(index, System.currentTimeMillis());
  }

  // Removes any items that are no longer displayed in the list because they
  // were completed more than delaySec second ago.
  private void removeStaleItems() {
    long removeBefore = System.currentTimeMillis() - delaySec * 1000;

    // Inv: no stale items at index i+1 or later
    for (int i = items.size() - 1; i >= 0; i--) {
      if (0 <= completedAt.get(i) && completedAt.get(i) < removeBefore) {
        items.remove(i);
        completedAt.remove(i);
      }
    }
  }
}