package org.basex.query.func;

import static org.basex.query.QueryTokens.*;
import static org.basex.util.Token.*;
import java.io.File;
import org.basex.query.QueryContext;
import org.basex.query.item.QNm;
import org.basex.query.item.Str;
import org.basex.query.item.Value;
import org.basex.query.util.Var;

/**
 * XQuery variables specified in modules.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Christian Gruen
 */
public enum VarDef {

  /* FNFile variables. */

  /** XQuery function */
  FILEDIRSEP(FILEURI, "directory-separator", Str.get(File.separator)),
  /** XQuery function */
  FILEPATHSEP(FILEURI, "path-separator", Str.get(File.pathSeparator));

  /** Variable name. */
  final QNm qname;
  /** Variable value. */
  final Value value;

  /**
   * Constructor.
   * @param uri uri
   * @param name name
   * @param val item value
   */
  private VarDef(final byte[] uri, final String name, final Value val) {
    qname = new QNm(token(name), uri);
    value = val;
  }

  /**
   * Initializes all variables.
   * @param ctx query context
   */
  public static void init(final QueryContext ctx) {
    for(final VarDef v : values()) {
      ctx.vars.setGlobal(Var.create(ctx, null, v.qname, v.value));
    }
  }
}
