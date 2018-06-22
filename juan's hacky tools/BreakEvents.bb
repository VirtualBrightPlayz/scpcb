Function CreateEvent(eventname$, roomname$, id%, prob# = 0.0)
	Local eventsFile% = ReadFile("OldEvents.bb")
	Local foundEventCode% = False
	Local ln$ = ""
	
	Local roomFile% = OpenFile("Rooms - Copy/"+roomname+".bb")
	If roomFile=0 Then
		RuntimeError(roomname+" isn't a bb file")
	EndIf
	SeekFile(roomFile,FileSize("Rooms - Copy/"+roomname+".bb"))
	
	Local capeventname$ = Upper(Left(eventname,1))+Right(eventname,Len(eventname)-1)
	WriteLine(roomFile,"") : WriteLine(roomFile,"")
	WriteLine(roomFile,"Function UpdateEvent"+capeventname+"(e.Events)")
	WriteLine(roomFile,Chr(9)+"Local dist#, i%, temp%, pvt%, strtemp$, j%, k%")
	WriteLine(roomFile,"")
	WriteLine(roomFile,Chr(9)+"Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams")
	WriteLine(roomFile,"")
	WriteLine(roomFile,Chr(9)+"Local CurrTrigger$ = "+Chr(34)+Chr(34))
	WriteLine(roomFile,"")
	WriteLine(roomFile,Chr(9)+"Local x#, y#, z#")
	WriteLine(roomFile,"")
	WriteLine(roomFile,Chr(9)+"Local angle#")
	WriteLine(roomFile,"")
	
	While Not Eof(eventsFile)
		ln = ReadLine(eventsFile)
		If (Instr(ln,Chr(9)+Chr(9)+Chr(9)+"Case "+Chr(34)+eventname+Chr(34))=1) Then
			ln = ReadLine(eventsFile)
			While (Instr(ln,Chr(9)+Chr(9)+Chr(9)+"Case ")<>1 And (Not Eof(eventsFile)))
				If Len(ln)>3 Then
					WriteLine(roomFile,Right(ln,Len(ln)-3))
				Else
					WriteLine(roomFile,"")
				EndIf
				ln = ReadLine(eventsFile)
			Wend
		EndIf
	Wend
	
	WriteLine(roomFile,"End Function")
	WriteLine(roomFile,"")

	CloseFile(roomFile)
	CloseFile(eventsFile)
	
	Local newEventsFile% = OpenFile("NewEvents - Copy.bb")
	SeekFile(newEventsFile,FileSize("NewEvents - Copy.bb"))
	WriteLine(newEventsFile,Chr(9)+Chr(9)+Chr(9)+"Case "+Chr(34)+eventname+Chr(34))
	WriteLine(newEventsFile,Chr(9)+Chr(9)+Chr(9)+Chr(9)+"UpdateEvent"+capeventname+"(e)")
	CloseFile(newEventsFile)
End Function

Function InitEvents()
	CreateEvent("173", "roomintro", 0)
	
	CreateEvent("alarm", "room173", 0)
	
	CreateEvent("pocketdimension", "roompocketdimension", 0)	
	
	;there's a 7% chance that 106 appears in the rooms named "tunnel"
	CreateEvent("tunnel106", "roomtunnel", 0, 0.07)
	
	;the chance for 173 appearing in the first lockroom is about 66%
	;there's a 30% chance that it appears in the later lockrooms
	CreateEvent("lockroom173", "roomlockroom", 0, 0.3)
	
	CreateEvent("room2trick", "room2", 0, 0.15)	
	
	CreateEvent("1048a", "room2", 0, 1.0)	
	
	CreateEvent("room2storage", "room2storage", 0)	
	
	;096 spawns in the first (and last) lockroom2
	CreateEvent("lockroom096", "roomlockroom2", 0)
	
	CreateEvent("endroom106", "roomendroom", Rand(0,1))
	
	CreateEvent("room2poffices2", "room2poffices2", 0)
	
	CreateEvent("room2fan", "room2_2", 0, 1.0)
	
	CreateEvent("room2elevator2", "room2elevator", 0)
	;CreateEvent("room2elevator", "room2elevator", 0, 1)
	CreateEvent("room2elevator", "room2elevator", Rand(1,2))
	
	CreateEvent("room3storage", "room3storage", 0, 0)
	
	CreateEvent("tunnel2smoke", "roomtunnel2", 0, 0.2)
	CreateEvent("tunnel2", "roomtunnel2", Rand(0,2), 0)
	;CreateEvent("tunnel2", "roomtunnel2", 0, 0)
	
	;173 appears in half of the "room2doors" -rooms
	CreateEvent("room2doors173", "room2doors", 0, 0.5)
	
	;the anomalous duck in room2offices2-rooms
	CreateEvent("room2offices2", "room2offices2", 0, 0.7)
	
	CreateEvent("room2closets", "room2closets", 0)	
	
	CreateEvent("room2cafeteria", "room2cafeteria", 0)	
	
	CreateEvent("room3pitduck", "room3pit", 0)
	CreateEvent("room3pit1048", "room3pit", 1)
	
	;the event that causes the door to open by itself in room2offices3
	CreateEvent("room2offices3", "room2offices3", 0, 1.0)	
	
	CreateEvent("room2servers", "room2servers", 0)	
	
	CreateEvent("room3servers", "room3servers", 0)	
	;CreateEvent("room3servers", "room3servers2", 0)
	
	;the dead guard
	CreateEvent("room3tunnel","room3tunnel", 0, 0.08)
	
	CreateEvent("room4","room4", 0)
	
	CreateEvent("682roar", "roomtunnel", Rand(0,2), 0)
	CreateEvent("testroom173", "room2testroom2", 0, 1.0)	
	
	CreateEvent("room2tesla", "room2tesla", 0, 0.9)
	
	CreateEvent("room2nuke", "room2nuke", 0, 0)
	
	CreateEvent("coffin106", "room895", 0, 0)
	CreateEvent("coffin", "room895", 0, 0)
	CreateEvent("checkpoint", "roomcheckpoint1", 0, 1.0)
	;CreateEvent("checkpoint", "roomcheckpoint2", 0, 1.0)
	
	CreateEvent("room3door", "room3", 0, 0.1)
	;CreateEvent("room3door", "room3tunnel", 0, 0.1)	
	
	CreateEvent("106victim", "room3", Rand(1,2))
	CreateEvent("106sinkhole", "room3_2", Rand(2,3))
	;CreateEvent("106sinkhole", "room4", Rand(1,2))
	
	CreateEvent("room079", "room079", 0, 0)	
	
	CreateEvent("room049", "room049", 0, 0)
	
	CreateEvent("room012", "room012", 0, 0)
	
	CreateEvent("room035", "room035", 0, 0)
	
	CreateEvent("008", "room008", 0, 0)
	
	CreateEvent("room106", "room106", 0, 0)	
	
	CreateEvent("pj", "room372", 0, 0)
	
	CreateEvent("914", "room914", 0, 0)
	
	CreateEvent("toiletguard", "room2toilets", 1)
	CreateEvent("buttghost", "room2toilets", 0, 0.8)
	
	CreateEvent("room2pipes106", "room2pipes", Rand(0, 3)) 
	
	CreateEvent("room2pit", "room2pit", 0, 0.4)
	
	CreateEvent("testroom", "roomtestroom", 0)
	
	CreateEvent("room2tunnel", "room2tunnel", 0)
	
	CreateEvent("room2ccont", "room2ccont", 0)
	
	CreateEvent("gateaentrance", "roomgateaentrance", 0)
	CreateEvent("gatea", "roomgatea", 0)	
	CreateEvent("exit1", "roomgateb", 0)
	
	CreateEvent("room205", "room205", 0)
	
	CreateEvent("room860","room860", 0)
	
	CreateEvent("room966","room966", 0)
	
	CreateEvent("room1123", "room1123", 0, 0)

	;CreateEvent("room2tesla", "room2tesla_lcz", 0, 0.9)
	;CreateEvent("room2tesla", "room2tesla_hcz", 0, 0.9)
	
	CreateEvent("room4tunnels","room4tunnels",0)
	CreateEvent("room_gw","room2gw",0,1.0)
	CreateEvent("dimension1499","roomdimension1499",0)
	CreateEvent("room1162","room1162",0)
	CreateEvent("room2scps2","room2scps2",0)
	;CreateEvent("room_gw","room3gw",0,1.0)
	CreateEvent("room2sl","room2sl",0)
	
	CreateEvent("room2gw_b","room2gw",Rand(0,1))
	
	CreateEvent("096spawn","room4pit",0,0.6)
	;CreateEvent("096spawn","room3pit",0,0.6)
	;CreateEvent("096spawn","room2pipes",0,0.4)
	;CreateEvent("096spawn","room2pit",0,0.5)
	;CreateEvent("096spawn","room3tunnel",0,0.6)
	;CreateEvent("096spawn","room4tunnels",0,0.7)
	;CreateEvent("096spawn","tunnel",0,0.6)
	;CreateEvent("096spawn","tunnel2",0,0.4)
	;CreateEvent("096spawn","room3z2",0,0.7)
	
	;CreateEvent("room2pit","room2_4",0,0.4)
	
End Function

InitEvents()
