package org.basex.query.up.primitives;

import static org.basex.query.QueryText.*;
import static org.basex.query.up.primitives.UpdatePrimitive.Type.*;

import org.basex.data.Data;
import org.basex.data.MemData;
import org.basex.query.QueryException;
import org.basex.query.item.DBNode;
import org.basex.query.item.Nod;
import org.basex.query.iter.Iter;
import org.basex.query.up.UpdateFunctions;
import org.basex.query.util.Err;

/**
 * Represents a replace primitive.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Lukas Kircher
 */
public final class ReplacePrimitive extends NodeCopyPrimitive {
  /** Target node is an attribute. */
  public boolean a;

  /**
   * Constructor.
   * @param n target node
   * @param replace replace nodes
   * @param attr replacing nodes are attributes
   */
  public ReplacePrimitive(final Nod n, final Iter replace, 
      final boolean attr) {
    super(n, replace);
    a = attr;
  }
  
  @SuppressWarnings("unused")
  @Override
  public void check() throws QueryException {
  }

  @Override
  public void apply() throws QueryException {
    if(!(node instanceof DBNode)) return;
      
    final MemData m = buildDB();
    final DBNode n = (DBNode) node;
    final Data d = n.data;
    // source nodes may be empty, thus the replace results in deleting the 
    // target node
    if(m == null) {
      d.delete(n.pre);
      return;
    }
    final int k = Nod.kind(n.type);
    final int par = d.parent(n.pre, d.kind(n.pre));
    final int s = m.size(0, Data.DOC) - 1;
    if(a)
      UpdateFunctions.insertAttributes(n.pre, par, d, m);
    else d.insertSeq(n.pre + d.size(n.pre, k), par , m);
    d.delete(n.pre + (a ? s : 0));
  }

  @Override
  public Type type() {
    return REPLACENODE;
  }
  
  @Override
  public void merge(final UpdatePrimitive p) throws QueryException {
    if(mult) Err.or(UPTRGMULT, node);
    mult = true;
  }
}