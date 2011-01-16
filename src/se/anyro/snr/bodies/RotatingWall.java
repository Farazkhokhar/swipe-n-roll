package se.anyro.snr.bodies;

import se.anyro.snr.Physics;
import se.anyro.snr.R;
import se.anyro.snr.SwipeNRoll;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class RotatingWall extends Block {

	private Joint mJoint;

	public RotatingWall(float x, float y, float width, float height) {
		super(x, y, width, height);
		
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.bodyA = Physics.getGround();
		jointDef.bodyB = mBody;
		jointDef.localAnchorA.set(x, y);
		jointDef.localAnchorB.set(0, 0);
		mJoint = Physics.createJoint(jointDef);
		
		mDrawable = SwipeNRoll.resources.getDrawable(R.drawable.wall_normal);
	}

	@Override
	public void destroy() {
		Physics.destroyJoint(mJoint);
		super.destroy();
	}
}
