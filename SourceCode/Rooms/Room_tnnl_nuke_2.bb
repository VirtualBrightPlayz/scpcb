Function FillRoom_tnnl_nuke_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    ;"tuulikaapin" ovi
    d = CreateDoor(r\zone, r\x + 576.0 * RoomScale, 0.0, r\z - 152.0 * RoomScale, 90, r, False, False, 5)
    d\autoClose = False : d\open = False
    PositionEntity(d\buttons[0], r\x + 608.0 * RoomScale, EntityY(d\buttons[0],True), r\z - 284.0 * RoomScale,True)
    PositionEntity(d\buttons[1], r\x + 544.0 * RoomScale, EntityY(d\buttons[1],True), r\z - 284.0 * RoomScale,True)			
    
    d = CreateDoor(r\zone, r\x - 544.0 * RoomScale, 1504.0*RoomScale, r\z + 738.0 * RoomScale, 90, r, False, False, 5)
    d\autoClose = False : d\open = False			
    PositionEntity(d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), r\z + 608.0 * RoomScale,True)
    PositionEntity(d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), r\z + 608.0 * RoomScale,True)
    
    ;yl�kerran hissin ovi
    r\roomDoors[0] = CreateDoor(r\zone, r\x + 1192.0 * RoomScale, 0.0, r\z, 90, r, True)
    r\roomDoors[0]\autoClose = False : r\roomDoors[0]\open = True
    ;yl�kerran hissi
    r\objects[4] = CreatePivot()
    PositionEntity(r\objects[4], r\x + 1496.0 * RoomScale, 240.0 * RoomScale, r\z)
    EntityParent(r\objects[4], r\obj)
    ;alakerran hissin ovi
    r\roomDoors[1] = CreateDoor(r\zone, r\x + 680.0 * RoomScale, 1504.0 * RoomScale, r\z, 90, r, False)
    r\roomDoors[1]\autoClose = False : r\roomDoors[1]\open = False
    ;alakerran hissi
    r\objects[5] = CreatePivot()
    PositionEntity(r\objects[5], r\x + 984.0 * RoomScale, 1744.0 * RoomScale, r\z)
    EntityParent(r\objects[5], r\obj)
    
	Local n%
    For n% = 0 To 1
        r\levers[n] = CreateLever()
        
        ScaleEntity(r\levers[n]\obj, 0.04, 0.04, 0.04)
        ScaleEntity(r\levers[n]\baseObj, 0.04, 0.04, 0.04)
        PositionEntity (r\levers[n]\obj, r\x - 975.0 * RoomScale, r\y + 1712.0 * RoomScale, r\z - (502.0-132.0*n) * RoomScale, True)
        PositionEntity (r\levers[n]\baseObj, r\x - 975.0 * RoomScale, r\y + 1712.0 * RoomScale, r\z - (502.0-132.0*n) * RoomScale, True)
        
        EntityParent(r\levers[n]\obj, r\obj)
        EntityParent(r\levers[n]\baseObj, r\obj)

        RotateEntity(r\levers[n]\baseObj, 0, -90-180, 0)
        RotateEntity(r\levers[n]\obj, 10, -90 - 180-180, 0)
        
        ;EntityPickMode(r\levers[n]\obj, 2)
        EntityPickMode r\levers[n]\obj, 1, False
        EntityRadius r\levers[n]\obj, 0.1
        ;makecollbox(r\levers[n]\obj)
    Next
    
    it = CreateItem("Nuclear Device Document", "paper", r\x - 768.0 * RoomScale, r\y + 1684.0 * RoomScale, r\z - 768.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Ballistic Vest", "vest", r\x - 944.0 * RoomScale, r\y + 1652.0 * RoomScale, r\z - 656.0 * RoomScale)
    EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, -90, 0)
    
    it = CreateItem("Dr L's Note", "paper", r\x + 800.0 * RoomScale, 88.0 * RoomScale, r\z + 256.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    sc.SecurityCams = CreateSecurityCam(r\x+624.0*RoomScale, r\y+1888.0*RoomScale, r\z-312.0*RoomScale, r)
    sc\angle = 90
    sc\turn = 45
    TurnEntity(sc\cameraObj, 20, 0, 0)
    sc\id = 6
End Function


Function UpdateEvent_tnnl_nuke_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		e\eventState2 = UpdateElevators(e\eventState2, e\room\roomDoors[0], e\room\roomDoors[1], e\room\objects[4], e\room\objects[5], e)
		
		e\eventState = e\room\levers[0]\succ
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D