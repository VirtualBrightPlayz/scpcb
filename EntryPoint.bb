Include "SourceCode/Main.bb"

Main()

; LoadMesh Example 
; ---------------- 

;Graphics3D 1280,720 
;SetBuffer BackBuffer() 
;
;camera=CreateCamera() 
;
;light=CreateLight() 
;RotateEntity light,90,0,0 
;
;; Load mesh 
;drum=LoadMesh("GFX/Map/Meshes/Button.b3d") 
;
;PositionEntity drum,0,0,MeshDepth(drum)*3
;
;For i% = 1 To CountSurfaces(drum)
;	Local ent_surface% = GetSurface(drum, i) 
;	Local ent_brush% = GetSurfaceBrush(ent_surface)
;	
;	For j% = 0 To 7
;		Local ent_texture% = GetBrushTexture(ent_brush, j)
;		
;		; Get texture path.
;		k$ = TextureName$(ent_texture)
;		If (k <> "") Then 
;			DebugLog(k)
;		EndIf
;		
;		FreeTexture(ent_texture)
;	Next
;	
;	FreeBrush(ent_brush)
;Next
;
;While Not KeyDown( 1 ) 
;	RenderWorld 
;	Flip 
;Wend 
;
;End

;~IDEal Editor Parameters:
;~C#Blitz3D