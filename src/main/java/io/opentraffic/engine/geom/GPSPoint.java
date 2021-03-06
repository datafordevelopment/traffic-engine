package io.opentraffic.engine.geom;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import io.opentraffic.engine.data.stores.SpatialDataStore;
import org.mapdb.Fun;

import java.io.Serializable;

public class GPSPoint implements Serializable,  Comparable<GPSPoint>  {

	private static final long serialVersionUID = 1l;

	public long time;
	public long vehicleId;
	public double lon;
	public double lat;
	public boolean convertToLocaltime;

	public GPSPoint(long time, long vehicleId, double lon, double lat) {
		this(time, vehicleId, lon, lat, true);
	}

	public GPSPoint(long time, long vehicleId, double lon, double lat, boolean convertToLocaltime) {

		// convert seconds to milliseconds
		if(time < 15000000000l)
			time = time * 1000;

		this.convertToLocaltime = convertToLocaltime;
		this.time = time;
		this.vehicleId = vehicleId;
		this.lon = lon;
		this.lat = lat;
	}

	public void offsetTime(long offset) {
		if(convertToLocaltime)
			this.time += offset;
	}

	public Point getPoint() {
		GeometryFactory gf = new GeometryFactory();
		return gf.createPoint(new Coordinate(lon, lat));
	}

	@Override
	public int compareTo(GPSPoint gpsPoint) {
		return Long.compare(this.time, gpsPoint.time);
	}

	public Fun.Tuple2<Integer, Integer> getTile() {

		return SpatialDataStore.getTile(lat, lon);
	}
}
