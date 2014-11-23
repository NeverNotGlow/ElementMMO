package com.imglow.ElementMMO;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OtherPlayer extends Player{

	@Override
	public void draw(SpriteBatch sb) {
		updateSprite();

		sb.draw(spr, (x - Game.getInstance().dX) * Cell.LENGTH, (y - Game.getInstance().dY) * Cell.LENGTH, WIDTH, HEIGHT); 	
	}

	protected void updateSprite() {
		TextureSingleton ts = TextureSingleton.getInstance();
		time++;
		time %= 14;

		if(time < 7)
			frame1 = true;
		else
			frame1 = false;

		if(moving)
		{
			if(moveDirection == LEFT) //moving left
			{
				if(frame1)
					spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_LEFT_1);
				else
					spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_LEFT_2);

				lw = LEFT;
			}
			else if(moveDirection == RIGHT) //moving right
			{
				if(frame1)
					spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_RIGHT_1);
				else
					spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_RIGHT_2);

				lw = RIGHT;
			}
			else if(moveDirection == DOWN) //moving down
			{
				if(frame1)
					spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_DOWN);
				else
					spr = ts.playerSprites.get(type).get(TextureSingleton.STAND);

				lw = DOWN;
			}
			else if(moveDirection == UP) //moving up
			{
				if(frame1)
					spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_UP);
				else
					spr = ts.playerSprites.get(type).get(TextureSingleton.FACE_UP);

				lw = UP;
			}
		}
		else
		{
			if(moveDirection == UP)
				spr = ts.playerSprites.get(type).get(TextureSingleton.FACE_UP);
			else if(moveDirection == DOWN)
				spr = ts.playerSprites.get(type).get(TextureSingleton.STAND);
			else if(moveDirection == RIGHT)
				spr = ts.playerSprites.get(type).get(TextureSingleton.FACE_RIGHT);
			else
				spr = ts.playerSprites.get(type).get(TextureSingleton.FACE_LEFT);
		}

		px = x;
		py = y;
	}

}
