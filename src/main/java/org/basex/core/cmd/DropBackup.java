package org.basex.core.cmd;

import static org.basex.core.Commands.*;
import static org.basex.core.Text.*;

import org.basex.core.Command;
import org.basex.core.CommandBuilder;
import org.basex.core.Context;
import org.basex.core.User;
import org.basex.core.Commands.Cmd;
import org.basex.data.MetaData;
import org.basex.io.IO;
import org.basex.io.IOFile;

/**
 * Evaluates the 'drop backup' command and deletes backups of a database.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Christian Gruen
 */
public final class DropBackup extends Command {
  /**
   * Default constructor.
   * @param name name of database
   */
  public DropBackup(final String name) {
    super(User.CREATE, name);
  }

  @Override
  protected boolean run() {
    if(!MetaData.validName(args[0], true)) return error(NAMEINVALID, args[0]);

    // loop through all databases and drop backups
    for(final String db : databases(args[0])) {
      drop(db.contains("-") ? db : db + '-', context);
    }
    return info(DBBACKDROP, args[0] + "*" + IO.ZIPSUFFIX);
  }

  /**
   * Drops one or more backups of the specified database.
   * @param db database
   * @param ctx database context
   * @return number of dropped backups
   */
  public static int drop(final String db, final Context ctx) {
    final IOFile dir = ctx.mprop.dbpath();
    int c = 0;
    for(final IOFile f : dir.children()) {
      final String n = f.name();
      if(n.startsWith(db) && n.endsWith(IO.ZIPSUFFIX)) {
        if(f.delete()) c++;
      }
    }
    return c;
  }

  @Override
  public void build(final CommandBuilder cb) {
    cb.init(Cmd.DROP + " " + CmdDrop.BACKUP).args();
  }
}
