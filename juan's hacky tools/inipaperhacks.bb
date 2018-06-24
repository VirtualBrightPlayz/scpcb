IniHackTool("Document SCP-079", "doc079.jpg")
IniHackTool("SCP-093 Recovered Materials", "doc093rm.jpg")
IniHackTool("Document SCP-106", "doc106.jpg")
IniHackTool("Recall Protocol RP-106-N", "docRecall.jpg")
IniHackTool("Document SCP-682", "doc682.jpg")
IniHackTool("Document SCP-173", "doc173.jpg")
IniHackTool("Document SCP-049", "doc049.jpg")
IniHackTool("Document SCP-096", "doc096.jpg")
IniHackTool("Document SCP-008", "doc008.jpg")
IniHackTool("Document SCP-012", "doc012.jpg")
IniHackTool("Document SCP-035", "doc035.jpg")
IniHackTool("SCP-035 Addendum", "doc035ad.jpg")
IniHackTool("Document SCP-860", "doc860.jpg")
IniHackTool("Document SCP-860-1", "doc8601.jpg")
IniHackTool("Document SCP-895", "doc895.jpg")
IniHackTool("Document SCP-939", "doc939.jpg")
IniHackTool("Document SCP-966", "doc966.jpg")
IniHackTool("Document SCP-970", "doc970.jpg")
IniHackTool("Document SCP-1048", "doc1048.jpg")
IniHackTool("Document SCP-1123", "doc1123.jpg")
IniHackTool("Document SCP-1162", "doc1162.jpg")

IniHackTool("Leaflet", "leaflet.jpg")

IniHackTool("Journal Page", "journal.jpg")


IniHackTool("Log(#1", "f4.jpg"))
IniHackTool("Log(#2", "f5.jpg"))
IniHackTool("Log(#3", "f6.jpg"))

IniHackTool("Nuclear Device Document", "docWar.jpg")
IniHackTool("Class D Orientation Leaflet", "docORI.jpg")


IniHackTool("Burnt Note", "docBurn.jpg")


IniHackTool("Mobile Task Forces", "docMTF.jpg")
IniHackTool("Security Clearance Levels", "docSC.jpg")
IniHackTool("Object Classes", "docOBJC.jpg")
IniHackTool("Incident Report SCP-106-0204", "docIR106.jpg")


IniHackTool("Disciplinary Hearing DH-S-4137-17092", "docDH.jpg")

IniHackTool("Note", "radical_linguini.jpg")
IniHackTool("Notification", "914_organic.jpg")
IniHackTool("Mysterious Note", "big_reveal.jpg")
IniHackTool("Note from Daniel", "docdan.jpg")
IniHackTool("Dr. Allok's Note", "106_treats.jpg")

IniHackTool("Strange Note", "docStrange.jpg")

IniHackTool("Dr. L's Note", "docL1.jpg")
IniHackTool("Dr L's Note", "docL2.jpg")
IniHackTool("Blood-stained Note", "docL3.jpg")
IniHackTool("Dr. L's Burnt Note", "docL4.jpg")
IniHackTool("Dr L's Burnt Note", "docL5.jpg")
IniHackTool("Scorched Note", "docL6.jpg")

Function IniHackTool(name$, fileName$)
	PutINIValue("Data/Items/paper.ini", fileName, "name", name)
End Function