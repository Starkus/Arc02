// Textures
uniform sampler2D tex;
uniform sampler2D norTex;
uniform sampler2D specTex;
uniform samplerCube refTex;

// Material
uniform float tex_mix;
uniform vec4 matAmbient;
uniform vec4 matDiffuse;
uniform vec4 matSpecular;

// Lights
uniform vec4 lightPos;
uniform vec4 lightAmb;
uniform vec4 lightDiff;
uniform vec4 lightSpec;

// Texture mix factors
uniform float tex_fac;
uniform float nor_fac;
uniform float spec_fac;
uniform float ref_fac;

// Camera
uniform vec3 cameraPosition;

// Geometry info
varying vec4 vVertex;
varying vec4 fVertex;
varying vec2 vUv;
varying vec4 vNormal;
varying vec3 fNormal;

varying vec3 vReflect;


float Rand(vec2 co) {
	float a = 1552.9898;
	float b = 78.233;
	float c = 43758.5453;
	float dt= dot(co.xy, vec2(a, b));
	float sn= mod(dt, 3.14);
	return fract(sin(sn) * c);
}


float emition() {
	return matAmbient.w;
}

vec4 normalmap(vec4 vnorm) {
	vec4 ntex = texture2D(norTex, vUv) * 2.0;
	vec4 nmix = mix(vnorm, (vnorm * vec4(ntex.rgb, 1.0)), nor_fac);
	
	return normalize(vec4(nmix));
}

vec3 ambient() {
	return matAmbient.xyz;
}

float diffuseLight() {
	
	vec4 nor = normalmap(vNormal);
	float lum;
	
	if (lightPos.w > 0) {
		vec4 lightNor = normalize(lightPos - fVertex);
		lum = max(dot(nor, lightNor), 0.0);
	} else {
		lum = max(dot(nor, lightPos), 0.0);
	}
	
	lum = 0.1 + clamp(lum, 0.0, 1.0);
	
	return lum + emition();
}
	
vec3 diffuse(float diffLight) {

	vec4 color = mix(matDiffuse, vec4(texture2D(tex, vUv)), tex_fac);
	
	return vec3(color * diffLight) * mix(vec3(1.0), lightDiff.rgb, lightDiff.w);
}

vec3 specular() {

	vec4 lightNor = normalize(lightPos - fVertex);
	vec3 viewDirection = normalize(cameraPosition - vVertex);

	vec4 nor = normalmap(vNormal);

	vec3 angle = normalize(viewDirection + lightNor);
	float Ispec = max(0.0, dot(nor, angle));
	Ispec = pow(Ispec, max(1.0, matSpecular.w)) * 2;
	vec3 spec = vec3(matSpecular.xyz * Ispec * lightSpec.xyz);
	return spec * mix(vec3(1.0), texture2D(specTex, vUv).xyz, spec_fac);
}

vec4 reflection(float diffLight) {
	vec3 ntex = vec3(texture2D(norTex, vUv)) * nor_fac;
	vec3 nor = fNormal + ntex;

	vec3 map = vec3(.5*nor.x+.5, -.5*nor.y+.5, nor.z);
	vec4 reflColor = textureCube(refTex, normalmap(vec4(vReflect.x, vReflect.z, vReflect.y, 0)).xyz);
	
	vec3 eye = normalize(-vVertex.xyz);
	float rim = smoothstep(0.0, 2.0, 1.5 - dot(fNormal, eye)) * diffLight;
	
	return vec4(reflColor.xyz * mix(vec3(1.0), lightDiff.rgb, lightDiff.w), rim * ref_fac);
}



void main() {
	float diffLight = diffuseLight();
	
	vec3 amb = ambient();
	vec3 diff = diffuse(diffLight);
	vec3 spec = specular();
	spec = spec * clamp(Rand(vec2(vVertex.xy)), 0.5, 1.0);
	
	vec4 refl = reflection(diffLight);
	
	vec3 final = mix((amb + diff + spec), refl, refl.w);
	float final_a = mix(matDiffuse.w, texture2D(tex, vUv).w, tex_fac);

	gl_FragColor = vec4(final, final_a);
}