Const DECAL_COUNT% = 15

Const DECAL_CORROSION%      = 0
Const DECAL_CRACKS%         = 1
Const DECAL_BLOOD_SPREAD%   = 2
Const DECAL_BLOOD_SPLATTER% = 3
Const DECAL_BLOOD_POOL%     = 4
Const DECAL_PD1%            = 5
Const DECAL_PD2%            = 6
Const DECAL_PD3%            = 7
Const DECAL_PD4%            = 8
Const DECAL_PD5%            = 9
Const DECAL_PD6%            = 10
Const DECAL_BULLET_HOLE1%   = 11
Const DECAL_BULLET_HOLE2%   = 12
Const DECAL_BLOOD_DROP1%    = 13
Const DECAL_BLOOD_DROP2%    = 14

Global decalList$[DECAL_COUNT]

Function LoadDecals()
    decalList[DECAL_CORROSION] = "corrosion.png"
    decalList[DECAL_CRACKS] = "cracks.png"
    decalList[DECAL_BLOOD_SPREAD] = "blood_spread.png"
    decalList[DECAL_BLOOD_SPLATTER] = "blood_splatter.png"
    decalList[DECAL_BLOOD_POOL] = "blood_pool.png"
    decalList[DECAL_PD1] = "decal_pd1.jpg"
    decalList[DECAL_PD2] = "decal_pd2.jpg"
    decalList[DECAL_PD3] = "decal_pd3.jpg"
    decalList[DECAL_PD4] = "decal_pd4.jpg"
    decalList[DECAL_PD5] = "decal_pd5.jpg"
    decalList[DECAL_PD6] = "decal_pd6.jpg"
    decalList[DECAL_BULLET_HOLE1] = "bullet_hole1.jpg"
    decalList[DECAL_BULLET_HOLE2] = "bullet_hole2.jpg"
    decalList[DECAL_BLOOD_DROP1] = "blood_drop1.png"
    decalList[DECAL_BLOOD_DROP2] = "blood_drop2.png"
End Function

Type Decal
	Field obj%
	Field sizeChange#, size#, maxSize#
	Field alphaChange#, alpha#
	Field blendmode%
	Field fx%
	Field id%
	Field timer#

	Field lifetime#

	Field x#, y#, z#
	Field pitch#, yaw#, roll#
End Type

Function CreateDecal.Decal(id%, x#, y#, z#, pitch#, yaw#, roll#)
    Local tex% = GrabTexture("GFX/Decals/" + decalList[id], 1+2)
	Local d.Decal = New Decal

	d\x = x
	d\y = y
	d\z = z
	d\pitch = pitch
	d\yaw = yaw
	d\roll = roll

	d\maxSize = 1.0

	d\alpha = 1.0
	d\size = 1.0
	d\obj = CreateSprite()
	d\blendmode = 1

	EntityTexture(d\obj, tex)
	EntityFX(d\obj, 0)
	SpriteViewMode(d\obj, 2)
	PositionEntity(d\obj, x, y, z)
	RotateEntity(d\obj, pitch, yaw, roll)
    DropAsset(tex)

	d\id = id

	If (tex = 0 Or d\obj = 0) Then Return Null

	Return d
End Function

Function UpdateDecals()
	Local angle#, temp#

	Local d.Decal, d2.Decal
	For d = Each Decal
		If (d\sizeChange <> 0) Then
			d\size=d\size + d\sizeChange * timing\tickDuration
			ScaleSprite(d\obj, d\size, d\size)

			Select (d\id)
				Case DECAL_CORROSION
					If (d\timer <= 0) Then
						angle = Rand(360)
						temp = Rnd(d\size)
						d2 = CreateDecal(DECAL_CRACKS, EntityX(d\obj) + Cos(angle) * temp, EntityY(d\obj) - 0.0005, EntityZ(d\obj) + Sin(angle) * temp, EntityPitch(d\obj), Rnd(360), EntityRoll(d\obj))
						d2\size = Rnd(0.1, 0.5) : ScaleSprite(d2\obj, d2\size, d2\size)
						;TODO: fix, and not attach this to the decal
						;PlayRangedSound(DecaySFX(Rand(1, 3)), mainPlayer\cam, d2\obj, 10.0, Rnd(0.1, 0.5))
						;d\timer = d\timer + Rand(50,150)
						d\timer = Rand(50, 100)
					Else
						d\timer= d\timer-timing\tickDuration
					EndIf
				;Case 6
				;	EntityBlend(d\obj, 2)
			End Select

			If (d\size >= d\maxSize) Then
				d\sizeChange = 0
				d\size = d\maxSize
			EndIf
		EndIf

		If (d\alphaChange <> 0) Then
			d\alpha = Min(d\alpha + timing\tickDuration * d\alphaChange, 1.0)
			EntityAlpha(d\obj, d\alpha)
		EndIf

		If (d\lifetime > 0) Then
			d\lifetime=Max(d\lifetime-timing\tickDuration,5)
		EndIf

		If (d\size <= 0 Or d\alpha <= 0 Or d\lifetime=5.0 ) Then
			FreeEntity(d\obj)
			Delete d
		EndIf
	Next
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D