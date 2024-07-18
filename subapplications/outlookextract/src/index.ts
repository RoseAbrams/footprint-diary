import { PSTMessage } from "pst-extractor";
import { PSTFile } from "pst-extractor";
import { PSTFolder } from "pst-extractor";
import * as fs from "fs";
const resolve = require("path").resolve;

let depth = -1;
let col = 0;

const pstFile = new PSTFile(
  resolve(
    "C:/Users/RosaAbrahamsson/Downloads/outlook backup 2023-05-04 - 8fbe75217ef14aa3a4fdfc010ace07f9.pst"
  )
);
//example();
//explore();
exportDecompiled("Inbox");
exportDecompiled("Sent Items");

function explore() {
  for (let folderL1 of pstFile.getRootFolder().getSubFolders()) {
    if (folderL1.displayName == "Top of Personal Folders") {
      for (let folderL2 of folderL1.getSubFolders()) {
        if (
          folderL2.displayName == "Inbox" ||
          folderL2.displayName == "Sent Items"
        ) {
          console.log("files in folder: " + folderL2.contentCount);
          console.log("emails in folder: " + folderL2.emailCount);
          while (true) {
            let email: PSTMessage = folderL2.getNextChild();
            if (email == null) break;
            console.log(email.clientSubmitTime);
          }
        }
        console.log("---");
      }
    }
  }
}

function exportDecompiled(folderName: string) {
  const output = [];
  for (let folderL1 of pstFile.getRootFolder().getSubFolders()) {
    if (folderL1.displayName == "Top of Personal Folders") {
      for (let folderL2 of folderL1.getSubFolders()) {
        if (folderL2.displayName == folderName) {
          while (true) {
            let email: PSTMessage = folderL2.getNextChild();
            if (email == null) break;
            output.push(email.toJSON());
          }
        }
      }
    }
  }
  fs.writeFile(
    `C:/Users/RosaAbrahamsson/Downloads/pst decompiled (${folderName}).json`,
    JSON.stringify(output),
    "utf8",
    () => {}
  );
}

/// https://github.com/epfromer/pst-extractor
function example() {
  console.log(pstFile.getMessageStore().displayName);
  processFolder(pstFile.getRootFolder());

  function processFolder(folder: PSTFolder) {
    depth++;

    // the root folder doesn't have a display name
    if (depth > 0) {
      console.log(getDepth(depth) + folder.displayName);
    }

    // go through the folders...
    if (folder.hasSubfolders) {
      let childFolders: PSTFolder[] = folder.getSubFolders();
      for (let childFolder of childFolders) {
        processFolder(childFolder);
      }
    }

    // and now the emails for this folder
    if (folder.contentCount > 0) {
      depth++;
      let email: PSTMessage = folder.getNextChild();
      while (email != null) {
        console.log(
          getDepth(depth) +
            "Sender: " +
            email.senderName +
            ", Subject: " +
            email.subject
        );
        email = folder.getNextChild();
      }
      depth--;
    }
    depth--;
  }

  function getDepth(depth: number): string {
    let sdepth = "";
    if (col > 0) {
      col = 0;
      sdepth += "\n";
    }
    for (let x = 0; x < depth - 1; x++) {
      sdepth += " | ";
    }
    sdepth += " |- ";
    return sdepth;
  }
}
