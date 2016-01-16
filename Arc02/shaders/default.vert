
// Matrices
uniform mat4 model;

// Camera
uniform vec3 cameraPosition;

varying vec4 vVertex;
varying vec4 fVertex;
varying vec2 vUv;
varying vec4 vNormal;
varying vec3 fNormal;

varying vec3 vReflect;



void main(void) {
	
	gl_Position = gl_ModelViewProjectionMatrix * (model * gl_Vertex);
	
	fVertex = gl_Vertex;
	vVertex = vec4(gl_ModelViewMatrix * gl_Vertex);
	
	vUv = vec2(gl_MultiTexCoord0);
	vNormal = model * vec4(gl_Normal, 0);
	fNormal = normalize(gl_NormalMatrix * mat3(model) * gl_Normal);
	
	vec3 worldPosition = (gl_Vertex).xyz;
	vec3 cameraToVertex = normalize(worldPosition - cameraPosition);
	vec3 worldNormal = normalize(
		mat3(model[0].xyz, model[1].xyz, model[2].xyz) * gl_Normal
	);
	vReflect = reflect(cameraToVertex, worldNormal);
}